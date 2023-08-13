package xyz.brassgoggledcoders.skyships.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.content.SkyShipsEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class SkyShip extends Entity {
    private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(
            Pose.STANDING, ImmutableList.of(0, 1, -1),
            Pose.CROUCHING, ImmutableList.of(0, 1, -1),
            Pose.SWIMMING, ImmutableList.of(0, 1)
    );

    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(SkyShip.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_HURT_DIR = SynchedEntityData.defineId(SkyShip.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(SkyShip.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_LEFT = SynchedEntityData.defineId(SkyShip.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_RIGHT = SynchedEntityData.defineId(SkyShip.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME = SynchedEntityData.defineId(SkyShip.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_VERTICAL = SynchedEntityData.defineId(SkyShip.class, EntityDataSerializers.INT);

    private final float[] paddlePositions = new float[2];
    private float outOfControlTicks;
    private float deltaRotation;

    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;

    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;
    private int inputVertical;

    private double waterLevel;
    private float landFriction;

    private SkyShipStatus status;
    private SkyShipStatus oldStatus;
    private double lastYd;

    public SkyShip(EntityType<?> type, Level level) {
        super(type, level);
    }

    public SkyShip(Level level, Vec3 location) {
        this(SkyShipsEntities.SKY_SHIP.get(), level, location);
        this.setPos(location.x(), location.y(), location.z());
    }

    public SkyShip(EntityType<?> entityType, Level level, Vec3 location) {
        super(entityType, level);
        this.setPos(location.x(), location.y(), location.z());
    }

    @Override
    @ParametersAreNonnullByDefault
    protected float getEyeHeight(Pose pPos, EntityDimensions entitySize) {
        return entitySize.height;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0);
        this.entityData.define(DATA_ID_HURT_DIR, 1);
        this.entityData.define(DATA_ID_DAMAGE, 0.0F);
        this.entityData.define(DATA_ID_PADDLE_LEFT, false);
        this.entityData.define(DATA_ID_PADDLE_RIGHT, false);
        this.entityData.define(DATA_ID_BUBBLE_TIME, 0);
        this.entityData.define(DATA_ID_VERTICAL, 0);
    }

    @Override
    public boolean canCollideWith(@Nonnull Entity pEntity) {
        return canVehicleCollide(this, pEntity);
    }

    public static boolean canVehicleCollide(Entity boat, Entity other) {
        return (other.canBeCollidedWith() || other.isPushable()) && !boat.isPassengerOfSameVehicle(other);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (this.outOfControlTicks < 60.0F) {
            if (!this.level.isClientSide) {
                return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean startRiding(@Nonnull Entity pVehicle, boolean force) {
        boolean startRiding = super.startRiding(pVehicle, force);
        if (startRiding) {
            this.setPaddleState(false, false, 0);
        }
        return startRiding;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle result) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, result));
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.2D;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (!this.level.isClientSide && this.isAlive()) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + pAmount * 10.0F);
            this.markHurt();
            boolean flag = pSource.getEntity() instanceof Player && ((Player) pSource.getEntity()).getAbilities().instabuild;
            if (flag || this.getDamage() > 40.0F) {
                if (!flag && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.destroy(pSource);
                }

                this.discard();
            }

            return true;
        } else {
            return true;
        }
    }

    protected void destroy(DamageSource pDamageSource) {
        this.kill();
        this.spawnLoot(pDamageSource);
    }

    private void spawnLoot(DamageSource pSource) {
        if (this.level instanceof ServerLevel serverLevel) {
            LootContext lootContext = new LootContext.Builder(serverLevel)
                    .withRandom(this.random)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.DAMAGE_SOURCE, pSource)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withOptionalParameter(LootContextParams.KILLER_ENTITY, pSource.getEntity())
                    .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, pSource.getDirectEntity())
                    .create(LootContextParamSets.ENTITY);

            serverLevel.getServer()
                    .getLootTables()
                    .get(this.getType().getDefaultLootTable())
                    .getRandomItems(lootContext)
                    .forEach(this::spawnAtLocation);
        }
    }

    @Override
    public void push(@Nonnull Entity pEntity) {
        if (pEntity instanceof Boat || pEntity instanceof SkyShip) {
            if (pEntity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.push(pEntity);
            }
        } else if (pEntity.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.push(pEntity);
        }
    }

    @Override
    public void animateHurt() {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = pYaw;
        this.lerpXRot = pPitch;
        this.lerpSteps = 10;
    }

    @Override
    public void tick() {
        this.oldStatus = this.status;
        this.status = this.getStatus();
        if (this.status != SkyShipStatus.UNDER_FLUID && this.status != SkyShipStatus.UNDER_FLOWING_FLUID) {
            this.outOfControlTicks = 0.0F;
        } else {
            ++this.outOfControlTicks;
        }

        if (!this.level.isClientSide && this.outOfControlTicks >= 60.0F) {
            this.ejectPassengers();
        }

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        super.tick();
        this.tickLerp();
        if (this.isControlledByLocalInstance()) {
            if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof Player)) {
                this.setPaddleState(false, false, 0);
            }

            this.floatBoat();
            if (this.level.isClientSide) {
                this.controlBoat();
                SkyShips.networkHandler.updateSkyShipControl(this.getPaddleState(0), this.getPaddleState(1), this.inputVertical);
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        for (int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent() && (double) (this.paddlePositions[i] % ((float) Math.PI * 2F)) <= (double) ((float) Math.PI / 4F) && ((double) this.paddlePositions[i] + (double) ((float) Math.PI / 8F)) % (double) ((float) Math.PI * 2F) >= (double) ((float) Math.PI / 4F)) {
                    SoundEvent soundevent = this.getPaddleSound();
                    if (soundevent != null) {
                        Vec3 vector3d = this.getViewVector(1.0F);
                        double d0 = i == 1 ? -vector3d.z : vector3d.z;
                        double d1 = i == 1 ? vector3d.x : -vector3d.x;
                        this.level.playSound(null, this.getX() + d0, this.getY(), this.getZ() + d1, soundevent, this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
                    }
                }

                this.paddlePositions[i] = (float) ((double) this.paddlePositions[i] + (double) ((float) Math.PI / 8F));
            }
        }

        this.checkInsideBlocks();
        List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.level.isClientSide && !(this.getControllingPassenger() instanceof Player);

            for (Entity entity : list) {
                if (!entity.hasPassenger(this)) {
                    if (flag && this.getPassengers().size() < 2 && !entity.isPassenger() &&
                            entity.getBbWidth() < this.getBbWidth() && entity instanceof LivingEntity &&
                            !(entity instanceof Player)) {
                        entity.startRiding(this);
                    } else {
                        this.push(entity);
                    }
                }
            }
        }

        resetFall(this);
    }

    private void resetFall(Entity entity) {
        entity.resetFallDistance();
        for (Entity passenger : entity.getPassengers()) {
            passenger.resetFallDistance();
        }
    }

    private void floatBoat() {
        int controlOffset = Integer.compare(this.entityData.get(DATA_ID_VERTICAL), 0);
        double verticalAddition = controlOffset != 0 ? controlOffset * 0.015D : this.isNoGravity() ? 0.0D : (double) -0.0004F;
        double d2 = 0.0D;
        float invFriction = 0.05F;
        if (this.oldStatus == SkyShipStatus.IN_AIR && this.status != SkyShipStatus.IN_AIR && this.status != SkyShipStatus.ON_LAND) {
            this.waterLevel = this.getY(1.0D);
            this.setPos(this.getX(), (double) (this.getWaterLevelAbove() - this.getBbHeight()) + 0.101D, this.getZ());
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            this.lastYd = 0.0D;
            this.status = SkyShipStatus.IN_FLUID;
        } else {
            if (this.status == SkyShipStatus.IN_FLUID) {
                d2 = (this.waterLevel - this.getY()) / (double) this.getBbHeight();
                invFriction = 0.45F;
            } else if (this.status == SkyShipStatus.UNDER_FLOWING_FLUID) {
                verticalAddition = -7.0E-4D;
                invFriction = 0.45F;
            } else if (this.status == SkyShipStatus.UNDER_FLUID) {
                d2 = 0.01F;
                invFriction = 0.25F;
            } else if (this.status == SkyShipStatus.IN_AIR) {
                invFriction = 0.7F;
            } else if (this.status == SkyShipStatus.ON_LAND) {
                invFriction = this.landFriction / 2;
                if (this.getControllingPassenger() instanceof Player) {
                    this.landFriction /= 2.0F;
                }
            }

            Vec3 vector3d = this.getDeltaMovement();
            this.setDeltaMovement(vector3d.x * (double) invFriction, vector3d.y + verticalAddition, vector3d.z * (double) invFriction);
            this.deltaRotation *= invFriction;
            if (d2 > 0.0D) {
                Vec3 vector3d1 = this.getDeltaMovement();
                this.setDeltaMovement(vector3d1.x, (vector3d1.y + d2 * 0.06153846016296973D) * 0.75D, vector3d1.z);
            }
        }

    }

    public float getWaterLevelAbove() {
        AABB axisalignedbb = this.getBoundingBox();
        int i = Mth.floor(axisalignedbb.minX);
        int j = Mth.ceil(axisalignedbb.maxX);
        int k = Mth.floor(axisalignedbb.maxY);
        int l = Mth.ceil(axisalignedbb.maxY - this.lastYd);
        int i1 = Mth.floor(axisalignedbb.minZ);
        int j1 = Mth.ceil(axisalignedbb.maxZ);
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

        label39:
        for (int k1 = k; k1 < l; ++k1) {
            float f = 0.0F;

            for (int l1 = i; l1 < j; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.set(l1, k1, i2);
                    FluidState fluidstate = this.level.getFluidState(blockpos$mutable);
                    if (fluidstate.is(FluidTags.WATER)) {
                        f = Math.max(f, fluidstate.getHeight(this.level, blockpos$mutable));
                    }

                    if (f >= 1.0F) {
                        continue label39;
                    }
                }
            }

            if (f < 1.0F) {
                return (float) blockpos$mutable.getY() + f;
            }
        }

        return (float) (l + 1);
    }


    protected SkyShipStatus getStatus() {
        SkyShipStatus status = this.isUnderFluid();
        if (status != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return status;
        } else if (this.checkInWater()) {
            return SkyShipStatus.IN_FLUID;
        } else {
            float f = this.getGroundFriction();
            if (f > 0.0F) {
                this.landFriction = f;
                return SkyShipStatus.ON_LAND;
            } else {
                return SkyShipStatus.IN_AIR;
            }
        }
    }

    public float getGroundFriction() {
        AABB boundingBox = this.getBoundingBox();
        AABB groundBoundingBox = new AABB(boundingBox.minX, boundingBox.minY - 0.001D, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        int i = Mth.floor(groundBoundingBox.minX) - 1;
        int j = Mth.ceil(groundBoundingBox.maxX) + 1;
        int k = Mth.floor(groundBoundingBox.minY) - 1;
        int l = Mth.ceil(groundBoundingBox.maxY) + 1;
        int i1 = Mth.floor(groundBoundingBox.minZ) - 1;
        int j1 = Mth.ceil(groundBoundingBox.maxZ) + 1;
        VoxelShape voxelshape = Shapes.create(groundBoundingBox);
        float f = 0.0F;
        int k1 = 0;
        BlockPos.MutableBlockPos checkPosition = new BlockPos.MutableBlockPos();

        for (int l1 = i; l1 < j; ++l1) {
            for (int i2 = i1; i2 < j1; ++i2) {
                int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
                if (j2 != 2) {
                    for (int k2 = k; k2 < l; ++k2) {
                        if (j2 <= 0 || k2 != k && k2 != l - 1) {
                            checkPosition.set(l1, k2, i2);
                            BlockState blockstate = this.level.getBlockState(checkPosition);
                            if (!(blockstate.getBlock() instanceof WaterlilyBlock) &&
                                    Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level, checkPosition).move(l1, k2, i2), voxelshape, BooleanOp.AND)) {
                                f += blockstate.getFriction(this.level, checkPosition, this);
                                ++k1;
                            }
                        }
                    }
                }
            }
        }

        return f / (float) k1;
    }

    private boolean checkInWater() {
        AABB axisalignedbb = this.getBoundingBox();
        int i = Mth.floor(axisalignedbb.minX);
        int j = Mth.ceil(axisalignedbb.maxX);
        int k = Mth.floor(axisalignedbb.minY);
        int l = Mth.ceil(axisalignedbb.minY + 0.001D);
        int i1 = Mth.floor(axisalignedbb.minZ);
        int j1 = Mth.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    mutableBlockPos.set(k1, l1, i2);
                    FluidState fluidstate = this.level.getFluidState(mutableBlockPos);
                    if (fluidstate.is(FluidTags.WATER)) {
                        float f = (float) l1 + fluidstate.getHeight(this.level, mutableBlockPos);
                        this.waterLevel = Math.max(f, this.waterLevel);
                        flag |= axisalignedbb.minY < (double) f;
                    }
                }
            }
        }

        return flag;
    }

    @Nullable
    private SkyShipStatus isUnderFluid() {
        AABB axisalignedbb = this.getBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001D;
        int i = Mth.floor(axisalignedbb.minX);
        int j = Mth.ceil(axisalignedbb.maxX);
        int k = Mth.floor(axisalignedbb.maxY);
        int l = Mth.ceil(d0);
        int i1 = Mth.floor(axisalignedbb.minZ);
        int j1 = Mth.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    mutablePos.set(k1, l1, i2);
                    FluidState fluidstate = this.level.getFluidState(mutablePos);
                    if (d0 < (double) ((float) mutablePos.getY() + fluidstate.getHeight(this.level, mutablePos))) {
                        if (!fluidstate.isSource()) {
                            return SkyShipStatus.UNDER_FLOWING_FLUID;
                        }

                        flag = true;
                    }
                }
            }
        }

        return flag ? SkyShipStatus.UNDER_FLUID : null;
    }

    protected SoundEvent getPaddleSound() {
        return null;
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
            this.setYRot((float) ((double) this.getYRot() + d3 / (double) this.lerpSteps));
            this.setXRot((float) ((double) this.getXRot() + (this.lerpXRot - (double) this.getYRot()) / (double) this.lerpSteps));
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }

    private void controlBoat() {
        if (this.isVehicle()) {
            float f = 0.0F;
            if (this.inputLeft) {
                --this.deltaRotation;
            }

            if (this.inputRight) {
                ++this.deltaRotation;
            }

            if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                f += 0.005F;
            }

            this.setYRot(this.getYRot() + this.deltaRotation);
            if (this.inputUp) {
                f += 0.15F;
            }

            if (this.inputDown) {
                f -= 0.05F;
            }

            Vec3 vector3d = this.getDeltaMovement();
            this.setDeltaMovement(new Vec3(
                    vector3d.x + Mth.sin(-this.getYRot() * ((float) Math.PI / 180F)) * f,
                    Integer.compare(this.inputVertical, 0) * 0.15F,
                    vector3d.z + Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * f)
            );
            this.setPaddleState(this.inputRight && !this.inputLeft || this.inputUp, this.inputLeft && !this.inputRight || this.inputUp, inputVertical);
        }
    }

    @Override
    public void positionRider(@Nonnull Entity pPassenger) {
        if (this.hasPassenger(pPassenger)) {
            float f = 0.0F;
            float f1 = (float) ((!this.isAlive() ? (double) 0.01F : this.getPassengersRidingOffset()) + pPassenger.getMyRidingOffset());
            if (this.getPassengers().size() == 1) {
                f = 0.4F;
            } else if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(pPassenger);
                if (i == 0) {
                    f = 0.4F;
                } else {
                    f = -0.6F;
                }

                if (pPassenger instanceof Animal) {
                    f = (float) ((double) f + 0.2D);
                }
            }

            Vec3 vector3d = (new Vec3(f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
            pPassenger.setPos(this.getX() + vector3d.x, this.getY() + (double) f1, this.getZ() + vector3d.z);
            pPassenger.setYRot(pPassenger.getYRot() + this.deltaRotation);
            pPassenger.setYHeadRot(pPassenger.getYHeadRot() + this.deltaRotation);
            this.clampRotation(pPassenger);
            if (pPassenger instanceof Animal && this.getPassengers().size() > 1) {
                int j = pPassenger.getId() % 2 == 0 ? 90 : 270;
                pPassenger.setYBodyRot(((Animal) pPassenger).yBodyRot + (float) j);
                pPassenger.setYHeadRot(pPassenger.getYHeadRot() + (float) j);
            }

        }
    }

    @Override
    public void onPassengerTurned(@Nonnull Entity pEntityToUpdate) {
        this.clampRotation(pEntityToUpdate);
    }

    protected void clampRotation(Entity pEntityToUpdate) {
        pEntityToUpdate.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(pEntityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        pEntityToUpdate.yRotO += f1 - f;
        pEntityToUpdate.setYRot(pEntityToUpdate.getYRot() + f1 - f);
        pEntityToUpdate.setYHeadRot(pEntityToUpdate.getYRot());
    }

    public void setPaddleState(boolean pLeft, boolean pRight, int vertical) {
        this.entityData.set(DATA_ID_PADDLE_LEFT, pLeft);
        this.entityData.set(DATA_ID_PADDLE_RIGHT, pRight);
        this.entityData.set(DATA_ID_VERTICAL, vertical);
    }

    @SuppressWarnings("unused")
    public float getRowingTime(int pSide, float pLimbSwing) {
        //return this.getPaddleState(pSide) ? (float) Mth.lerp(pLimbSwing, this.paddlePositions[pSide], (double) this.paddlePositions[pSide] - (double) ((float) Math.PI / 8F)) : this.paddlePositions[pSide];
        return (float) Mth.lerp(pLimbSwing, this.paddlePositions[pSide], (double) this.paddlePositions[pSide] - (double) ((float) Math.PI / 8F));
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {

    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void addPassenger(@Nonnull Entity passenger) {
        super.addPassenger(passenger);
        if (passenger instanceof Player && ((Player) passenger).isLocalPlayer()) {
            passenger.yRotO = this.getYRot();
            passenger.setYRot(this.getYRot());
            passenger.setYHeadRot(this.getYRot());
        }
        if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.absMoveTo(this.lerpX, this.lerpY, this.lerpZ, (float) this.lerpYRot, (float) this.lerpXRot);
        }
    }

    @Override
    protected boolean canAddPassenger(@Nonnull Entity pPassenger) {
        return this.getPassengers().size() < 2;
    }

    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    public void setInput(boolean pLeftInputDown, boolean pRightInputDown, boolean pForwardInputDown, boolean pBackInputDown, int vertical) {
        this.inputLeft = pLeftInputDown;
        this.inputRight = pRightInputDown;
        this.inputUp = pForwardInputDown;
        this.inputDown = pBackInputDown;
        this.inputVertical = vertical;
    }

    public boolean getPaddleState(int pSide) {
        return this.entityData.get(pSide == 0 ? DATA_ID_PADDLE_LEFT : DATA_ID_PADDLE_RIGHT) && this.getControllingPassenger() != null;
    }

    public void setDamage(float pDamageTaken) {
        this.entityData.set(DATA_ID_DAMAGE, pDamageTaken);
    }

    public float getDamage() {
        return this.entityData.get(DATA_ID_DAMAGE);
    }

    public void setHurtTime(int pTimeSinceHit) {
        this.entityData.set(DATA_ID_HURT, pTimeSinceHit);
    }

    public int getHurtTime() {
        return this.entityData.get(DATA_ID_HURT);
    }

    public void setHurtDir(int pForwardDirection) {
        this.entityData.set(DATA_ID_HURT_DIR, pForwardDirection);
    }

    public int getHurtDir() {
        return this.entityData.get(DATA_ID_HURT_DIR);
    }

    //Pulled from AbstractMinecart
    @Override
    @NotNull
    public Vec3 getDismountLocationForPassenger(@NotNull LivingEntity pLivingEntity) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] offsetsForDirection = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos possibleDismountPos = new BlockPos.MutableBlockPos();
            ImmutableList<Pose> dismountPoses = pLivingEntity.getDismountPoses();

            for (Pose pose : dismountPoses) {
                EntityDimensions entitydimensions = pLivingEntity.getDimensions(pose);
                float f = Math.min(entitydimensions.width, 1.0F) / 2.0F;

                for (int height : Objects.requireNonNull(POSE_DISMOUNT_HEIGHTS.get(pose))) {
                    for (int[] offsets : offsetsForDirection) {
                        possibleDismountPos.set(blockpos.getX() + offsets[0], blockpos.getY() + height, blockpos.getZ() + offsets[1]);
                        double d0 = this.level.getBlockFloorHeight(
                                DismountHelper.nonClimbableShape(this.level, possibleDismountPos),
                                () -> DismountHelper.nonClimbableShape(this.level, possibleDismountPos.below())
                        );
                        if (DismountHelper.isBlockFloorValid(d0)) {
                            AABB aabb = new AABB(-f, 0.0D, -f, f, entitydimensions.height, f);
                            Vec3 vec3 = Vec3.upFromBottomCenterOf(possibleDismountPos, d0);
                            if (DismountHelper.canDismountTo(this.level, pLivingEntity, aabb.move(vec3))) {
                                pLivingEntity.setPose(pose);
                                return vec3;
                            }
                        }
                    }
                }
            }

            double d1 = this.getBoundingBox().maxY;
            possibleDismountPos.set(blockpos.getX(), d1, blockpos.getZ());

            for (Pose dismountPose : dismountPoses) {
                double height = pLivingEntity.getDimensions(dismountPose).height;
                int roundedHeight = Mth.ceil(d1 - (double) possibleDismountPos.getY() + height);
                double ceilingPos = DismountHelper.findCeilingFrom(
                        possibleDismountPos,
                        roundedHeight,
                        (collisionPos) -> this.level.getBlockState(collisionPos).getCollisionShape(this.level, collisionPos)
                );
                if (d1 + height <= ceilingPos) {
                    pLivingEntity.setPose(dismountPose);
                    break;
                }
            }

        }
        return super.getDismountLocationForPassenger(pLivingEntity);
    }
}

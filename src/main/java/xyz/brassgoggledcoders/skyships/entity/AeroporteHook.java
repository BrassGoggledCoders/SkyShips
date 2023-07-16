package xyz.brassgoggledcoders.skyships.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import xyz.brassgoggledcoders.skyships.blockentity.AeroporteControllerBlockEntity;
import xyz.brassgoggledcoders.skyships.content.SkyShipsEntities;

import javax.annotation.Nonnull;

public class AeroporteHook extends Entity {
    private static final EntityDataAccessor<BlockPos> CONTROLLER_POS = SynchedEntityData.defineId(AeroporteHook.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Float> TARGET_HEIGHT = SynchedEntityData.defineId(AeroporteHook.class, EntityDataSerializers.FLOAT);

    private int controllerCheckTick = 0;
    private boolean releaseAtTarget;

    public AeroporteHook(EntityType<?> type, Level world) {
        super(type, world);
    }

    public AeroporteHook(Level world, Vec3 position) {
        this(SkyShipsEntities.AEROPORTE_HOOK.get(), world);
        this.setPos(position);
    }

    public void setControllerPos(BlockPos blockPos) {
        this.entityData.set(CONTROLLER_POS, blockPos);
    }

    public BlockPos getControllerPos() {
        return this.entityData.get(CONTROLLER_POS);
    }

    public void setTargetHeight(float height) {
        this.entityData.set(TARGET_HEIGHT, height);
    }

    public float getTargetHeight() {
        return this.entityData.get(TARGET_HEIGHT);
    }

    @Override
    public void tick() {
        if (this.getFirstPassenger() == null) {
            this.setRemoved(RemovalReason.KILLED);
        }

        if (this.controllerCheckTick-- <= 0) {
            if (!(this.getLevel().getBlockEntity(this.getControllerPos()) instanceof AeroporteControllerBlockEntity)) {
                this.setRemoved(RemovalReason.KILLED);
            }
            this.controllerCheckTick = this.getLevel().getRandom().nextInt(50);
        }

        if (Math.abs(this.getTargetHeight() - this.getY()) > 0.15F) {
            if (this.getTargetHeight() > this.getY()) {
                this.move(MoverType.SELF, new Vec3(0, 0.25F, 0));
            } else {
                this.move(MoverType.SELF, new Vec3(0, -0.25F, 0));
            }
        } else if (this.isReleaseAtTarget()) {
            this.getPassengers().forEach(Entity::stopRiding);
        }
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(CONTROLLER_POS, BlockPos.ZERO);
        this.entityData.define(TARGET_HEIGHT, 0F);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        this.setControllerPos(NbtUtils.readBlockPos(pCompound.getCompound("controllerPos")));
        this.setTargetHeight(pCompound.getFloat("TargetHeight"));
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        pCompound.put("controllerPos", NbtUtils.writeBlockPos(this.getControllerPos()));
        pCompound.putFloat("TargetHeight", this.getTargetHeight());
    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean shouldRender(double camX, double camY, double camZ) {
        return false;
    }

    public boolean isReleaseAtTarget() {
        return releaseAtTarget;
    }

    public void setReleaseAtTarget(boolean releaseAtTarget) {
        this.releaseAtTarget = releaseAtTarget;
    }
}

package xyz.brassgoggledcoders.skyships.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

public class AeroporteHook extends Entity {
    private final EntityDataAccessor<BlockPos> CONTROLLER_POS = SynchedEntityData.defineId(AeroporteHook.class, EntityDataSerializers.BLOCK_POS);

    public AeroporteHook(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CONTROLLER_POS, BlockPos.ZERO);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        this.entityData.define(CONTROLLER_POS, NbtUtils.readBlockPos(pCompound.getCompound("controllerPos")));
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        pCompound.put("controllerPos", NbtUtils.writeBlockPos(this.entityData.get(CONTROLLER_POS)));
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
}

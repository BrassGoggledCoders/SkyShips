package xyz.brassgoggledcoders.skyships.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class AeroporteHookEntity extends Entity {
    private final DataParameter<BlockPos> CONTROLLER_POS = EntityDataManager.defineId(AeroporteHookEntity.class, DataSerializers.BLOCK_POS);

    public AeroporteHookEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CONTROLLER_POS, BlockPos.ZERO);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundNBT pCompound) {
        this.entityData.define(CONTROLLER_POS, NBTUtil.readBlockPos(pCompound.getCompound("controllerPos")));
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundNBT pCompound) {
        pCompound.put("controllerPos", NBTUtil.writeBlockPos(this.entityData.get(CONTROLLER_POS)));
    }

    @Override
    @Nonnull
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean shouldRender(double camX, double camY, double camZ) {
        return false;
    }
}

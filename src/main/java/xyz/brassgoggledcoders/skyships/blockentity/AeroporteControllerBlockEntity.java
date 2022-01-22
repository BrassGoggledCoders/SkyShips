package xyz.brassgoggledcoders.skyships.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class AeroporteControllerBlockEntity extends BlockEntity {
    public AeroporteControllerBlockEntity(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public void handleDockingShip(Entity dockingEntity, Direction side) {

    }
}

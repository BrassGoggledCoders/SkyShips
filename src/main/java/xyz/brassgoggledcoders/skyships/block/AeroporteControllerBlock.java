package xyz.brassgoggledcoders.skyships.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import xyz.brassgoggledcoders.skyships.blockentity.AeroporteControllerBlockEntity;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class AeroporteControllerBlock extends Block {
    public AeroporteControllerBlock(Properties properties) {
        super(properties);
    }

    public void handleDockingShip(Entity dockingEntity, Level world, BlockPos blockPos, Direction side) {
        BlockEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity instanceof AeroporteControllerBlockEntity) {
            ((AeroporteControllerBlockEntity) tileEntity).handleDockingShip(dockingEntity, side);
        }
    }
}

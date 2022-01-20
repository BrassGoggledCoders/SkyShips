package xyz.brassgoggledcoders.skyships.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.skyships.blockentity.AeroporteControllerBlockEntity;

public class AeroporteControllerBlock extends Block {
    public AeroporteControllerBlock(Properties properties) {
        super(properties);
    }

    public void handleDockingShip(Entity dockingEntity, World world, BlockPos blockPos, Direction side) {
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity instanceof AeroporteControllerBlockEntity) {
            ((AeroporteControllerBlockEntity) tileEntity).handleDockingShip(dockingEntity, side);
        }
    }
}

package xyz.brassgoggledcoders.skyships.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.skyships.blockentity.AeroporteControllerBlockEntity;
import xyz.brassgoggledcoders.skyships.content.SkyShipsBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

public class AeroporteControllerBlock extends Block implements EntityBlock {
    public AeroporteControllerBlock(Properties properties) {
        super(properties);
    }

    public void handleDockingShip(Entity dockingEntity, Level world, BlockPos blockPos, Direction side) {
        if (world.getBlockEntity(blockPos) instanceof AeroporteControllerBlockEntity aeroporteControllerBlockEntity) {
            aeroporteControllerBlockEntity.handleDockingShip(dockingEntity, side);
        }
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return SkyShipsBlocks.AEROPORTE_CONTROLLER_BLOCK_ENTITY.create(pPos, pState);
    }
}

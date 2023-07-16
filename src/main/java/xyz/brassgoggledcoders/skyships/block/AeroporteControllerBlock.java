package xyz.brassgoggledcoders.skyships.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.skyships.blockentity.AeroporteControllerBlockEntity;
import xyz.brassgoggledcoders.skyships.content.SkyShipsBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

public class AeroporteControllerBlock extends Block implements EntityBlock {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public AeroporteControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TRIGGERED);
    }

    public void handleDockingShip(Entity dockingEntity, Level world, BlockPos blockPos, Direction side) {
        if (world.getBlockEntity(blockPos) instanceof AeroporteControllerBlockEntity aeroporteControllerBlockEntity) {
            aeroporteControllerBlockEntity.handleDockingShip(dockingEntity, side);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean flag = pLevel.hasNeighborSignal(pPos) || pLevel.hasNeighborSignal(pPos.above());
        boolean flag1 = pState.getValue(TRIGGERED);
        if (flag && !flag1) {
            pLevel.scheduleTick(pPos, this, 4);
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.TRUE), 4);
        } else if (!flag && flag1) {
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.FALSE), 4);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof AeroporteControllerBlockEntity blockEntity) {
            blockEntity.handleReleaseShip();
        }
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return SkyShipsBlocks.AEROPORTE_CONTROLLER_BLOCK_ENTITY.create(pPos, pState);
    }
}

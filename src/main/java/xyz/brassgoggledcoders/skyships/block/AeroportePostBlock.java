package xyz.brassgoggledcoders.skyships.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import xyz.brassgoggledcoders.skyships.content.SkyShipsEntityTags;
import xyz.brassgoggledcoders.skyships.entity.AeroporteHook;
import xyz.brassgoggledcoders.skyships.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class AeroportePostBlock extends Block {
    public static final EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 16, 10);

    public AeroportePostBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(HORIZONTAL_FACING, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockState placedOn = context.getLevel()
                .getBlockState(context.getClickedPos()
                        .relative(context.getClickedFace()
                                .getOpposite()
                        )
                );
        if (placedOn.hasProperty(HORIZONTAL_FACING)) {
            facing = placedOn.getValue(HORIZONTAL_FACING);
        }

        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, facing)
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState blockState, BlockGetter blockReader, BlockPos blockPos, CollisionContext selectionContext) {
        return SHAPE;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return !pState.getValue(WATERLOGGED);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return pState;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        return state.setValue(HORIZONTAL_FACING, direction.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!(entity.getVehicle() instanceof AeroporteHook) && entity.getType().is(SkyShipsEntityTags.DOCKABLE)) {
            BlockPos furthestDown = BlockHelper.findFurthest(blockPos, level, Direction.DOWN, testState -> testState == blockState);
            if (level.getBlockState(furthestDown).getBlock() instanceof AeroporteControllerBlock aeroporteControllerBlock) {
                aeroporteControllerBlock.handleDockingShip(entity, level, furthestDown, Direction.DOWN);
            } else {
                BlockPos furthestUp = BlockHelper.findFurthest(blockPos, level, Direction.UP, testState -> testState == blockState);
                if (level.getBlockState(furthestUp).getBlock() instanceof AeroporteControllerBlock aeroporteControllerBlock) {
                    aeroporteControllerBlock.handleDockingShip(entity, level, furthestUp, Direction.UP);
                }
            }
        }
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }
}

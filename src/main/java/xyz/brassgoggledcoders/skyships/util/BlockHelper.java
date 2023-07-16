package xyz.brassgoggledcoders.skyships.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.skyships.block.AeroportePostBlock;

import java.util.function.Predicate;

public class BlockHelper {
    public static BlockPos findFurthest(BlockPos startPos, Level level, Direction heading, Predicate<BlockState> matches) {
        BlockPos.MutableBlockPos mutableFurthest = startPos.mutable().move(heading);
        BlockState nextState = level.getBlockState(mutableFurthest);
        while (nextState.getBlock() instanceof AeroportePostBlock && matches.test(nextState)) {
            nextState = level.getBlockState(mutableFurthest.move(heading));
        }

        return mutableFurthest.immutable();
    }
}

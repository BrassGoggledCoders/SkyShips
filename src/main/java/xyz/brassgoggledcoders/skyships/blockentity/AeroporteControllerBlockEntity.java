package xyz.brassgoggledcoders.skyships.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import xyz.brassgoggledcoders.skyships.block.AeroportePostBlock;
import xyz.brassgoggledcoders.skyships.entity.AeroporteHook;
import xyz.brassgoggledcoders.skyships.util.BlockHelper;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AeroporteControllerBlockEntity extends BlockEntity {
    public AeroporteControllerBlockEntity(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public void handleDockingShip(Entity dockingEntity, Direction toController) {
        if (!(dockingEntity.getVehicle() instanceof AeroporteHook)) {
            float sizeOffSet = (dockingEntity.getBbWidth() / 2 * 1.75F);
            Vec3 entityPos = dockingEntity.getPosition(1.0F);
            Vec3 hookLocation = Arrays.stream(Direction.values())
                    .filter(direction -> direction.getAxis() != Direction.Axis.Y)
                    .map(direction -> new Vec3(
                            entityPos.x + direction.getStepX() * (sizeOffSet + 0.5),
                            entityPos.y,
                            entityPos.z + direction.getStepX() * (sizeOffSet + 0.5)
                    ))
                    .reduce(entityPos, (posA, posB) -> {
                        double distanceA = posA.distanceTo(entityPos);
                        double distanceB = posB.distanceTo(entityPos);

                        return distanceA < distanceB ? posA : posB;
                    });

            if (toController == Direction.UP) {
                double closeness = hookLocation.y() + dockingEntity.getBbHeight() - this.getBlockPos().getY();
                if (closeness > 0) {
                    hookLocation = hookLocation.subtract(0, closeness, 0);
                }
            }

            if (this.getLevel() != null) {
                AeroporteHook hook = new AeroporteHook(this.getLevel(), hookLocation);
                hook.setControllerPos(this.getBlockPos());
                float targetHeight = this.getBlockPos().getY();
                if (toController == Direction.UP) {
                    targetHeight -= (dockingEntity.getBbHeight() + 0.5F);
                } else {
                    targetHeight++;
                }

                hook.setTargetHeight(targetHeight);
                if (!this.getLevel().isClientSide()) {
                    this.getLevel().addFreshEntity(hook);
                    dockingEntity.startRiding(hook, true);
                }
            }
        }
    }

    public void handleReleaseShip() {
        if (this.getLevel() != null) {
            Set<AeroporteHook> activeHooks = this.getLevel().getEntities(
                            null,
                            AABB.ofSize(Vec3.atCenterOf(this.getBlockPos()), 5, 3, 5)
                    )
                    .stream()
                    .flatMap(entity -> {
                        if (entity instanceof AeroporteHook aeroporteHook) {
                            return Stream.of(aeroporteHook);
                        } else if (entity.getVehicle() instanceof AeroporteHook aeroporteHook) {
                            return Stream.of(aeroporteHook);
                        } else {
                            return Stream.empty();
                        }
                    })
                    .filter(aeroporteHook -> aeroporteHook.getControllerPos().equals(this.getBlockPos()))
                    .collect(Collectors.toUnmodifiableSet());

            if (!activeHooks.isEmpty()) {
                BlockPos highestPost = null;
                BlockPos lowestPost = null;

                for (AeroporteHook hook : activeHooks) {
                    if (hook.getY() > this.getBlockPos().getY()) {
                        if (highestPost == null) {
                            highestPost = this.getBlockPos().above();
                            BlockState aboveState = this.getLevel().getBlockState(highestPost);
                            if (aboveState.getBlock() instanceof AeroportePostBlock) {
                                highestPost = BlockHelper.findFurthest(
                                        highestPost,
                                        this.getLevel(),
                                        Direction.UP,
                                        blockState -> blockState == aboveState
                                );
                            }
                        }

                        hook.setTargetHeight(highestPost.getY());
                    } else {
                        if (lowestPost == null) {
                            BlockPos belowPos = this.getBlockPos().below();
                            BlockState belowState = this.getLevel().getBlockState(belowPos);
                            if (belowState.getBlock() instanceof AeroportePostBlock) {
                                lowestPost = BlockHelper.findFurthest(
                                        belowPos,
                                        this.getLevel(),
                                        Direction.DOWN,
                                        blockState -> blockState == belowState
                                );
                            } else {
                                lowestPost = this.getBlockPos().above();
                            }
                        }

                        hook.setTargetHeight(lowestPost.getY());
                    }
                    hook.setReleaseAtTarget(true);
                }
            }
        }

    }
}

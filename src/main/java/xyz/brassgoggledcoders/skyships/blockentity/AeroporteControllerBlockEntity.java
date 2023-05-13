package xyz.brassgoggledcoders.skyships.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import xyz.brassgoggledcoders.skyships.entity.AeroporteHook;

import java.util.Arrays;

public class AeroporteControllerBlockEntity extends BlockEntity {
    public AeroporteControllerBlockEntity(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public void handleDockingShip(Entity dockingEntity, Direction toController) {
        if (!(dockingEntity.getVehicle() instanceof AeroporteHook)) {
            float sizeOffSet = dockingEntity.getBbWidth() / 2;
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
                dockingEntity.startRiding(hook, true);
                this.getLevel().addFreshEntity(hook);
            }
        }
    }
}

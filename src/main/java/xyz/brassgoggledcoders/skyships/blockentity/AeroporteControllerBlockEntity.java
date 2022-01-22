package xyz.brassgoggledcoders.skyships.blockentity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;

public class AeroporteControllerBlockEntity extends BlockEntity {
    public AeroporteControllerBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    public void handleDockingShip(Entity dockingEntity, Direction side) {

    }
}

package xyz.brassgoggledcoders.skyships.blockentity;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class AeroporteControllerBlockEntity extends TileEntity {
    public AeroporteControllerBlockEntity(TileEntityType<?> type) {
        super(type);
    }

    public void handleDockingShip(Entity dockingEntity, Direction side) {

    }
}

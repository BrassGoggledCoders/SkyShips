package xyz.brassgoggledcoders.skyships.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;
import xyz.brassgoggledcoders.skyships.util.functional.Result;

import java.util.Optional;

public class Navigator {
    private final SkyShip skyShip;
    private final IItemHandler navigationInventory;
    private int currentSlot;
    @NotNull
    private Result<GlobalPos> destinationResult;

    public Navigator(SkyShip skyShip, IItemHandler navigationInventory) {
        this.skyShip = skyShip;
        this.navigationInventory = navigationInventory;
        this.destinationResult = Result.waiting();
    }

    public void navigate() {
        this.destinationResult = destinationResult.run(this::findDestination);

        if (this.destinationResult.isSuccess()) {
            GlobalPos destination = this.destinationResult.value();

            if (destination.dimension() == this.skyShip.getLevel().dimension()) {
                int vertical = 0;
                BlockPos entityPos = this.skyShip.getOnPos();
                int groundHeight = this.skyShip.getLevel().getHeight(Heightmap.Types.WORLD_SURFACE_WG, entityPos.getX(), entityPos.getZ());
                if (this.skyShip.getEyePosition().y() < groundHeight + 10) {
                    vertical = 1;
                }

                int yawDirection = 0;

                float delta = getDelta(destination);
                if (delta < 175 && delta > 5) {
                    yawDirection = 1;
                } else if (delta > 185 && delta < 355) {
                    yawDirection = -1;
                }

                this.skyShip.navigateBoat(yawDirection, yawDirection == 0 ? 1 : 0, vertical);
            }
        }
    }

    private float getDelta(GlobalPos destination) {
        BlockPos entityPos = this.skyShip.getOnPos();
        int xOffset = entityPos.getX() - destination.pos().getX();
        int zOffset = entityPos.getZ() - destination.pos().getZ();
        float expectedYaw = (float) (-Math.toDegrees(Math.atan2(xOffset, zOffset)) + 180F);

        float delta = expectedYaw - this.skyShip.getYRot();

        if (delta < 0) {
            delta += 360;
        } else if (delta > 360) {
            delta -= 360;
        }
        return delta;
    }

    public void resetChecks() {
        this.destinationResult = Result.waiting();
    }

    private Optional<GlobalPos> findDestination() {
        int maxAttempts = this.navigationInventory.getSlots();

        Optional<GlobalPos> globalPos = Optional.empty();
        int attempts = 0;
        while (globalPos.isEmpty() && attempts < maxAttempts) {
            int slot = (currentSlot + attempts) % maxAttempts;
            ItemStack itemStack = this.navigationInventory.getStackInSlot(slot);

            if (!itemStack.isEmpty()) {
                globalPos = NavigationProviderRegistry.getInstance()
                        .getPositionFor(itemStack, this.skyShip.getLevel());
            }

            if (globalPos.isPresent()) {
                this.currentSlot = slot;
            }
            attempts++;
        }
        return globalPos;
    }

}

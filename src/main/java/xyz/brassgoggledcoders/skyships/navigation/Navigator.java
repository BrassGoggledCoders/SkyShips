package xyz.brassgoggledcoders.skyships.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
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
                if (this.skyShip.getEyePosition().y() < this.skyShip.getLevel().getSeaLevel() + 50) {
                    vertical = 1;
                }

                boolean left = false;
                boolean right = false;

                float delta = getDelta(destination);
                if (delta < 175 && delta > 5) {
                    left = true;
                } else if (delta > 185 && delta < 355) {
                    right = true;
                } else {
                    left = true;
                    right = true;
                }

                this.skyShip.setPaddleState(left, right, vertical);
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
        }
        return globalPos;
    }

}

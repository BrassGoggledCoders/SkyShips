package xyz.brassgoggledcoders.skyships.navigation;

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
    private Result<GlobalPos> destination;

    public Navigator(SkyShip skyShip, IItemHandler navigationInventory) {
        this.skyShip = skyShip;
        this.navigationInventory = navigationInventory;
        this.destination = Result.waiting();
    }

    public void navigate() {
        this.destination = destination.run(this::findDestination);
    }

    public void resetChecks() {
        this.destination = Result.waiting();
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

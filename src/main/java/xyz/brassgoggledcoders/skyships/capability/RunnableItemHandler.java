package xyz.brassgoggledcoders.skyships.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public class RunnableItemHandler extends ItemStackHandler implements Iterable<ItemStack> {
    private final Runnable onChange;

    public RunnableItemHandler(int slots, Runnable onChange) {
        super(slots);
        this.onChange = onChange;
    }

    @Override
    protected void onContentsChanged(int slot) {
        onChange.run();
    }

    @NotNull
    @Override
    public Iterator<ItemStack> iterator() {
        return this.getStacks().iterator();
    }

    public Collection<ItemStack> getStacks() {
        return this.stacks;
    }
}




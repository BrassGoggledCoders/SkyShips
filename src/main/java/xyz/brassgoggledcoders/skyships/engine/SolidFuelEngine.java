package xyz.brassgoggledcoders.skyships.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

public class SolidFuelEngine extends Engine {
    public static final Codec<SolidFuelEngine> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("fuel").forGetter(SolidFuelEngine::getFuel),
            Codec.INT.fieldOf("burnRemaining").forGetter(SolidFuelEngine::getBurnRemaining),
            Codec.INT.fieldOf("maxBurn").forGetter(SolidFuelEngine::getMaxBurn)
    ).apply(instance, SolidFuelEngine::new));

    private ItemStack fuel;
    private int burnRemaining;
    private int maxBurn;

    public SolidFuelEngine() {
        this(ItemStack.EMPTY, 0, Integer.MAX_VALUE);
    }

    public SolidFuelEngine(ItemStack fuel, int burnRemaining, int maxBurn) {
        this.fuel = fuel;
        this.burnRemaining = burnRemaining;
        this.maxBurn = maxBurn;
    }

    @Override
    public boolean tryRun(SkyShip skyShip) {
        if (burnRemaining-- <= 0 && !this.fuel.isEmpty()) {
            int newMaxBurn = ForgeHooks.getBurnTime(this.fuel, null);
            if (newMaxBurn > 0) {
                this.maxBurn = newMaxBurn;
                if (this.fuel.hasCraftingRemainingItem()) {
                    this.fuel = this.fuel.getCraftingRemainingItem();
                } else {
                    this.fuel.shrink(1);
                }
            }
        }
        return burnRemaining > 0;
    }

    @Override
    @NotNull
    public Codec<? extends Engine> getCodec() {
        return CODEC;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public int getBurnRemaining() {
        return burnRemaining;
    }

    public int getMaxBurn() {
        return maxBurn;
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }
}

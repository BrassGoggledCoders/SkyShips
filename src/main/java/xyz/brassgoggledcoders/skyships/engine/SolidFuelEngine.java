package xyz.brassgoggledcoders.skyships.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
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
    public boolean canRun(SkyShip skyShip) {
        return false;
    }

    @Override
    @NotNull
    public Codec<? extends Engine> getCodec() {
        return CODEC;
    }

    public void setFuel(ItemStack fuel) {
        this.fuel = fuel;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public void setBurnRemaining(int burnRemaining) {
        this.burnRemaining = burnRemaining;
    }

    public int getBurnRemaining() {
        return burnRemaining;
    }

    public void setMaxBurn(int maxBurn) {
        this.maxBurn = maxBurn;
    }

    public int getMaxBurn() {
        return maxBurn;
    }
}

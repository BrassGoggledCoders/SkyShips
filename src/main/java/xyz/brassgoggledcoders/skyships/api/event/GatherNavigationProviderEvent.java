package xyz.brassgoggledcoders.skyships.api.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class GatherNavigationProviderEvent extends Event {
    private final List<Pair<Ingredient, BiFunction<ItemStack, Level, Optional<GlobalPos>>>> navigationProviders;

    public GatherNavigationProviderEvent() {
        this.navigationProviders = new ArrayList<>();
    }

    public void register(Ingredient ingredient, BiFunction<ItemStack, Level, Optional<GlobalPos>> provider) {
        this.navigationProviders.add(Pair.of(
                ingredient,
                provider
        ));
    }

    public List<Pair<Ingredient, BiFunction<ItemStack, Level, Optional<GlobalPos>>>> getNavigationProviders() {
        return navigationProviders;
    }
}

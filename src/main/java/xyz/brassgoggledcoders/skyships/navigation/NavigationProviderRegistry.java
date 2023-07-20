package xyz.brassgoggledcoders.skyships.navigation;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import xyz.brassgoggledcoders.skyships.api.event.GatherNavigationProviderEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class NavigationProviderRegistry {
    private static final NavigationProviderRegistry INSTANCE = new NavigationProviderRegistry();

    private final List<Pair<Ingredient, BiFunction<ItemStack, Level, Optional<GlobalPos>>>> navigationProviders;

    public NavigationProviderRegistry() {
        this.navigationProviders = new ArrayList<>();
    }

    public void register(Ingredient ingredient, BiFunction<ItemStack, Level, Optional<GlobalPos>> provider) {
        this.navigationProviders.add(Pair.of(
                ingredient,
                provider
        ));
    }

    public Optional<GlobalPos> getPositionFor(ItemStack itemStack, Level level) {
        return this.navigationProviders.parallelStream()
                .filter(navigationProvider -> navigationProvider.getFirst().test(itemStack))
                .flatMap(navigationProvider -> navigationProvider.getSecond()
                        .apply(itemStack, level)
                        .stream()
                )
                .findFirst();
    }

    public void gatherProviders() {
        this.navigationProviders.clear();
        GatherNavigationProviderEvent gatherNavigationProviderEvent = new GatherNavigationProviderEvent();
        MinecraftForge.EVENT_BUS.post(gatherNavigationProviderEvent);
        gatherNavigationProviderEvent.getNavigationProviders()
                .forEach(pair -> this.register(pair.getFirst(), pair.getSecond()));
    }

    public static NavigationProviderRegistry getInstance() {
        return INSTANCE;
    }
}

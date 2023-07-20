package xyz.brassgoggledcoders.skyships.eventhandler;

import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.api.event.GatherNavigationProviderEvent;
import xyz.brassgoggledcoders.skyships.navigation.NavigationProviderRegistry;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = SkyShips.ID, bus = Bus.FORGE)
public class ForgeCommonEventHandler {

    @SubscribeEvent
    public static void invalidNavigationIngredients(TagsUpdatedEvent event) {
        NavigationProviderRegistry.getInstance()
                .gatherProviders();
    }

    @SubscribeEvent
    public static void gatherProviders(GatherNavigationProviderEvent event) {
        event.register(
                Ingredient.of(Items.COMPASS),
                (itemStack, level) -> {
                    if (CompassItem.isLodestoneCompass(itemStack)) {
                        return Optional.ofNullable(CompassItem.getLodestonePosition(itemStack.getOrCreateTag()));
                    } else {
                        return Optional.ofNullable(CompassItem.getSpawnPosition(level));
                    }
                }
        );
    }
}

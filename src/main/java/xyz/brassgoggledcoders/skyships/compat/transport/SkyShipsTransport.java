package xyz.brassgoggledcoders.skyships.compat.transport;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.content.SkyShipsEntityTags;
import xyz.brassgoggledcoders.skyships.content.SkyShipsItems;
import xyz.brassgoggledcoders.skyships.registrate.NonLivingEntityLootTables;
import xyz.brassgoggledcoders.transport.data.recipe.ShellItemRecipeBuilder;

@SuppressWarnings("unused")
public class SkyShipsTransport {

    public static final EntityEntry<ShellSkyShip> SHELL_SKY_SHIP = SkyShips.getRegistrate()
            .object("shell_sky_ship")
            .<ShellSkyShip>entity(ShellSkyShip::new, MobCategory.MISC)
            .properties(skyShipEntityBuilder -> skyShipEntityBuilder.clientTrackingRange(10)
                    .sized(1.75f, 4f)
            )
            .renderer(() -> ShellSkyShipRenderer::new)
            .tag(SkyShipsEntityTags.DOCKABLE)
            .setData(ProviderType.LOOT, NonLivingEntityLootTables.loot(LootTable::lootTable))
            .setData(ProviderType.LANG, (context, provider) -> {
                provider.add(context.get().getDescriptionId(), provider.getAutomaticName(context, Registry.ENTITY_TYPE_REGISTRY));
                provider.add(context.get().getDescriptionId() + ".with", "Sky Ship with %s");
            })
            .register();

    public static final ItemEntry<ShellSkyShipItem> SHELL_SKY_SHIP_ITEM = SkyShips.getRegistrate()
            .object("shell_sky_ship")
            .item(ShellSkyShipItem::new)
            .model((context, provider) -> provider.generated(context, provider.mcLoc("item/oak_boat")))
            .recipe((context, provider) -> ShellItemRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(SkyShipsItems.SKY_SHIP.get()))
                    .save(provider, SkyShips.rl("shell_sky_ship"))
            )
            .register();

    public static void setup() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SkyShipsTransportClientEventHandler::setup);
    }
}

package xyz.brassgoggledcoders.skyships.content;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;
import xyz.brassgoggledcoders.skyships.registrate.NonLivingEntityLootTables;
import xyz.brassgoggledcoders.skyships.renderer.SkyShipRenderer;

@SuppressWarnings("unused")
public class SkyShipsEntities {
    public static EntityEntry<SkyShip> SKY_SHIP = SkyShips.getRegistrate()
            .object("sky_ship")
            .<SkyShip>entity(SkyShip::new, MobCategory.MISC)
            .properties(skyShipEntityBuilder -> skyShipEntityBuilder.clientTrackingRange(10)
                    .sized(1.75f, 4f)
            )
            .renderer(() -> SkyShipRenderer::new)
            .setData(ProviderType.LOOT, NonLivingEntityLootTables.loot(() -> LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(SkyShipsItems.SKY_SHIP.get()))
                    )
            ))
            .tag(SkyShipsEntityTags.DOCKABLE)
            .register();

    public static void setup() {

    }
}

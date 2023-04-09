package xyz.brassgoggledcoders.skyships.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.SkyShips;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NonLivingEntityLootTables extends RegistrateEntityLootTables {
    public NonLivingEntityLootTables(AbstractRegistrate<?> parent, Consumer<RegistrateEntityLootTables> callback) {
        super(parent, callback);
    }

    @Override
    protected boolean isNonLiving(@NotNull EntityType<?> entitytype) {
        return false;
    }

    public static <R extends Entity> NonNullBiConsumer<DataGenContext<EntityType<?>, EntityType<R>>, RegistrateLootTableProvider> loot(
            Supplier<LootTable.Builder> lootTable
    ) {
        return (context, provider) -> provider.addLootAction(
                SkyShips.NON_LIVING_ENTITY_LOOT_TYPE,
                tb -> tb.add(
                        context.get(),
                        lootTable.get()
                )
        );
    }
}

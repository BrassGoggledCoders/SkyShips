package xyz.brassgoggledcoders.skyships;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider.LootType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.brassgoggledcoders.skyships.content.SkyShipsBlocks;
import xyz.brassgoggledcoders.skyships.content.SkyShipsEngines;
import xyz.brassgoggledcoders.skyships.content.SkyShipsEntities;
import xyz.brassgoggledcoders.skyships.content.SkyShipsItems;
import xyz.brassgoggledcoders.skyships.network.NetworkHandler;
import xyz.brassgoggledcoders.skyships.registrate.NonLivingEntityLootTables;

import javax.annotation.Nonnull;

@Mod(SkyShips.ID)
public class SkyShips {
    public static final String ID = "skyships";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    private static final Lazy<Registrate> REGISTRATE_LAZY = Lazy.of(() -> Registrate.create(ID)
            .creativeModeTab(() -> new CreativeModeTab(ID) {
                @Override
                @Nonnull
                public ItemStack makeIcon() {
                    return Items.OAK_BOAT.getDefaultInstance();
                }
            }, "Sky Ships")
    );

    public static final Lazy<LootType<RegistrateEntityLootTables>> NON_LIVING_ENTITY_LOOT_TYPE = Lazy.of(() -> LootType.register(
            "non_living_entity",
            LootContextParamSets.ENTITY,
            NonLivingEntityLootTables::new
    ));

    public static NetworkHandler networkHandler;

    public SkyShips() {
        networkHandler = new NetworkHandler();

        SkyShipsBlocks.setup();
        SkyShipsEngines.setup();
        SkyShipsItems.setup();
        SkyShipsEntities.setup();
    }

    public static Registrate getRegistrate() {
        return REGISTRATE_LAZY.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

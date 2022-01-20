package xyz.brassgoggledcoders.skyships;

import com.tterrag.registrate.Registrate;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.skyships.content.SkyShipsBlocks;
import xyz.brassgoggledcoders.skyships.content.SkyShipsEntities;
import xyz.brassgoggledcoders.skyships.network.NetworkHandler;

import javax.annotation.Nonnull;

@Mod(SkyShips.ID)
public class SkyShips {
    public static final String ID = "skyships";

    private static final Lazy<Registrate> REGISTRATE_LAZY = Lazy.of(() -> Registrate.create(ID)
            .itemGroup(() -> new ItemGroup(ID) {
                @Override
                @Nonnull
                public ItemStack makeIcon() {
                    return Items.OAK_BOAT.getDefaultInstance();
                }
            }, "Sky Ships")
    );

    public static NetworkHandler networkHandler;

    public SkyShips() {
        networkHandler = new NetworkHandler();
        SkyShipsEntities.setup();
        SkyShipsBlocks.setup();
    }

    public static Registrate getRegistrate() {
        return REGISTRATE_LAZY.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

package xyz.brassgoggledcoders.skyships.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.item.SkyShipItem;

public class SkyShipsItems {

    public static ItemEntry<SkyShipItem> SKY_SHIP = SkyShips.getRegistrate()
            .object("sky_ship")
            .item(SkyShipItem::new)
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("WWW")
                    .pattern("S S")
                    .pattern(" B ")
                    .define('W', ItemTags.WOOL)
                    .define('S', Tags.Items.STRING)
                    .define('B', ItemTags.BOATS)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(ItemTags.BOATS))
                    .save(provider)
            )
            .model((context, provider) -> provider.generated(context, provider.mcLoc("item/oak_boat")))
            .register();

    public static void setup() {

    }
}

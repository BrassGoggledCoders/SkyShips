package xyz.brassgoggledcoders.skyships.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.block.AeroporteControllerBlock;
import xyz.brassgoggledcoders.skyships.block.AeroportePostBlock;
import xyz.brassgoggledcoders.skyships.blockentity.AeroporteControllerBlockEntity;

@SuppressWarnings("unused")
public class SkyShipsBlocks {
    public static final BlockEntry<AeroporteControllerBlock> AEROPORTE_CONTROLLER = SkyShips.getRegistrate()
            .object("aeroporte_controller")
            .block(AeroporteControllerBlock::new)
            .initialProperties(Material.METAL, MaterialColor.METAL)
            .properties(properties -> properties.sound(SoundType.METAL)
                    .strength(5.0F, 6.0F)
            )
            .blockstate((context, provider) -> provider.simpleBlock(
                    context.get(),
                    provider.models()
                            .cubeAll(
                                    context.getName(),
                                    provider.blockTexture(Blocks.IRON_BLOCK)
                            )
            ))
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("CCC")
                    .pattern("CPC")
                    .pattern("IRI")
                    .define('C', Items.CHAIN)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('P', Blocks.PISTON)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .unlockedBy("item", RegistrateRecipeProvider.hasItem(Items.CHAIN))
                    .save(provider)
            )
            .tileEntity(AeroporteControllerBlockEntity::new)
            .build()
            .item()
            .build()
            .register();

    public static BlockEntry<AeroportePostBlock> AEROPORTE_OAK_POST = SkyShips.getRegistrate()
            .object("aeroporte_oak_post")
            .block(AeroportePostBlock::new)
            .initialProperties(Material.WOOD)
            .properties(properties -> properties.noOcclusion()
                    .sound(SoundType.WOOD)
            )
            .addLayer(() -> RenderType::cutout)
            .blockstate((context, provider) -> provider.getMultipartBuilder(context.get())
                    .part()
                    .modelFile(provider.models().getExistingFile(provider.mcLoc("block/oak_fence_post")))
                    .addModel()
                    .end()
                    .part()
                    .modelFile(provider.models().getExistingFile(provider.modLoc("block/offset_chain")))
                    .addModel()
                    .condition(AeroportePostBlock.HORIZONTAL_FACING, Direction.NORTH)
                    .end()
                    .part()
                    .modelFile(provider.models().getExistingFile(provider.modLoc("block/offset_chain")))
                    .rotationY(180)
                    .addModel()
                    .condition(AeroportePostBlock.HORIZONTAL_FACING, Direction.SOUTH)
                    .end()
                    .part()
                    .modelFile(provider.models().getExistingFile(provider.modLoc("block/offset_chain")))
                    .rotationY(90)
                    .addModel()
                    .condition(AeroportePostBlock.HORIZONTAL_FACING, Direction.EAST)
                    .end()
                    .part()
                    .modelFile(provider.models().getExistingFile(provider.modLoc("block/offset_chain")))
                    .rotationY(270)
                    .addModel()
                    .condition(AeroportePostBlock.HORIZONTAL_FACING, Direction.WEST)
                    .end()
            )
            .item()
            .model((context, provider) -> provider.withExistingParent(
                    "item/aeroporte_oak_post",
                    provider.mcLoc("block/oak_fence_post")
            ))
            .recipe((context, provider) -> ShapelessRecipeBuilder.shapeless(context.get())
                    .requires(Items.OAK_FENCE)
                    .requires(Items.CHAIN)
                    .unlockedBy("item", RegistrateRecipeProvider.hasItem(Items.CHAIN))
                    .save(provider)
            )
            .build()
            .register();

    public static void setup() {

    }
}

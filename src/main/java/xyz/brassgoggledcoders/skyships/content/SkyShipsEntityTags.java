package xyz.brassgoggledcoders.skyships.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.skyships.SkyShips;

import java.util.Objects;

public class SkyShipsEntityTags {
    public static final TagKey<EntityType<?>> DOCKABLE = createTagKey("dockable");

    public static TagKey<EntityType<?>> createTagKey(String id, String path) {
        return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.tags())
                .createTagKey(new ResourceLocation(id, path));
    }

    public static TagKey<EntityType<?>> createTagKey(String path) {
        return createTagKey(SkyShips.ID, path);
    }
}

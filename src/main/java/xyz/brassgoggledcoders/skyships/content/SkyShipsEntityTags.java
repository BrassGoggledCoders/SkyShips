package xyz.brassgoggledcoders.skyships.content;

import net.minecraft.world.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import xyz.brassgoggledcoders.skyships.SkyShips;

public class SkyShipsEntityTags {
    public static final Tag.Named<EntityType<?>> DOCKABLE = EntityTypeTags.createOptional(SkyShips.rl("dockable"));
}

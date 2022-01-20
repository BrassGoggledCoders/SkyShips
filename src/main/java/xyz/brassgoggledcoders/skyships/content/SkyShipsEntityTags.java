package xyz.brassgoggledcoders.skyships.content;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import xyz.brassgoggledcoders.skyships.SkyShips;

public class SkyShipsEntityTags {
    public static final ITag.INamedTag<EntityType<?>> DOCKABLE = EntityTypeTags.createOptional(SkyShips.rl("dockable"));
}

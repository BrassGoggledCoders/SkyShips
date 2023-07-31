package xyz.brassgoggledcoders.skyships.content;

import com.google.common.collect.ImmutableSet;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import xyz.brassgoggledcoders.skyships.SkyShips;

public class SkyShipsPOITypes {

    public static RegistryEntry<PoiType> AEROPORTE = SkyShips.getRegistrate()
            .object("aeroporte")
            .simple(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, () -> new PoiType(
                    ImmutableSet.copyOf(SkyShipsBlocks.AEROPORTE_CONTROLLER.get()
                            .getStateDefinition()
                            .getPossibleStates()
                    ),
                    1,
                    1
            ));

    public static void setup() {

    }
}

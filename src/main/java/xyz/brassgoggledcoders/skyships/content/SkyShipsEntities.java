package xyz.brassgoggledcoders.skyships.content;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;
import xyz.brassgoggledcoders.skyships.renderer.SkyShipRenderer;

@SuppressWarnings("unused")
public class SkyShipsEntities {
    public static EntityEntry<SkyShip> SKY_SHIP = SkyShips.getRegistrate()
            .object("sky_ship")
            .entity(SkyShip::new, MobCategory.MISC)
            .properties(skyShipEntityBuilder -> skyShipEntityBuilder.clientTrackingRange(10)
                    .sized(1.375f, 0.5625f)
            )
            .renderer(() -> SkyShipRenderer::new)
            .tag(SkyShipsEntityTags.DOCKABLE)
            .register();

    public static void setup() {

    }
}

package xyz.brassgoggledcoders.skyships.content;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.entity.EntityClassification;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShipEntity;
import xyz.brassgoggledcoders.skyships.renderer.SkyShipRenderer;

@SuppressWarnings("unused")
public class SkyShipsEntities {
    public static EntityEntry<SkyShipEntity> SKY_SHIP = SkyShips.getRegistrate()
            .object("sky_ship")
            .entity(SkyShipEntity::new, EntityClassification.MISC)
            .properties(skyShipEntityBuilder -> skyShipEntityBuilder.clientTrackingRange(10)
                    .sized(1.375f, 0.5625f)
            )
            .renderer(() -> SkyShipRenderer::new)
            .register();

    public static void setup() {

    }
}

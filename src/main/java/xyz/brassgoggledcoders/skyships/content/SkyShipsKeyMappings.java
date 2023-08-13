package xyz.brassgoggledcoders.skyships.content;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.skyships.SkyShips;

@EventBusSubscriber(modid = SkyShips.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class SkyShipsKeyMappings {
    public static final KeyMapping DESCEND = new KeyMapping(
            SkyShipsText.getKey(SkyShipsText.DESCEND_KEY),
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            SkyShipsText.getKey(SkyShipsText.KEY_CATEGORY)
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(DESCEND);
    }
}

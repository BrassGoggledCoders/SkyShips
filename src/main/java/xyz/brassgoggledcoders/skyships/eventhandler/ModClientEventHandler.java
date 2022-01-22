package xyz.brassgoggledcoders.skyships.eventhandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.renderer.BalloonModel;
import xyz.brassgoggledcoders.skyships.renderer.GondolaModel;
import xyz.brassgoggledcoders.skyships.renderer.SkyShipRenderer;
import xyz.brassgoggledcoders.skyships.renderer.SteeringModel;

@EventBusSubscriber(modid = SkyShips.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SkyShipRenderer.BALLOON_LOCATION, BalloonModel::createBodyLayer);
        event.registerLayerDefinition(SkyShipRenderer.STEERING_LOCATION, SteeringModel::createBodyLayer);
        event.registerLayerDefinition(SkyShipRenderer.GONDOLA_LOCATION, GondolaModel::createBodyLayer);
    }
}

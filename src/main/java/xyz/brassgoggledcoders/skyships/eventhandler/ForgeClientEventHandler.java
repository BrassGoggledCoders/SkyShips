package xyz.brassgoggledcoders.skyships.eventhandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.Input;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.content.SkyShipsKeyMappings;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

@Mod.EventBusSubscriber(modid = SkyShips.ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void handlePaddleState(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START && Minecraft.getInstance().player != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player.getVehicle() instanceof SkyShip skyShip) {
                Input input = player.input;
                skyShip.setInput(
                        input.left,
                        input.right,
                        input.up,
                        input.down,
                        input.jumping ? 1 : SkyShipsKeyMappings.DESCEND.isDown() ? -1 : 0
                );
            }
        }
    }

    @SubscribeEvent
    public static void handleEnterShip(EntityMountEvent event) {
        if (event.isMounting() && event.getEntityBeingMounted() instanceof SkyShip shipEntity &&
                event.getEntityMounting() instanceof LocalPlayer player) {

            player.yRotO = shipEntity.getYRot();
            player.setYRot(shipEntity.getYRot());
            player.setYHeadRot(shipEntity.getYRot());
        }
    }
}

package xyz.brassgoggledcoders.skyships.eventhandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShipEntity;

@Mod.EventBusSubscriber(modid = SkyShips.ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void handlePaddleState(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START && Minecraft.getInstance().player != null) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player.getVehicle() instanceof SkyShipEntity) {
                MovementInput input = player.input;
                ((SkyShipEntity) player.getVehicle()).setInput(
                        input.left,
                        input.right,
                        input.up,
                        input.down,
                        input.jumping ? 1 : 0
                );
            }
        }
    }

    @SubscribeEvent
    public static void handleEnterShip(EntityMountEvent event) {
        if (event.isMounting() && event.getEntityBeingMounted() instanceof SkyShipEntity &&
                event.getEntityMounting() instanceof ClientPlayerEntity) {
            ClientPlayerEntity player = (ClientPlayerEntity) event.getEntityMounting();
            SkyShipEntity shipEntity = (SkyShipEntity) event.getEntityBeingMounted();

            player.yRotO = shipEntity.yRot;
            player.yRot = shipEntity.yRot;
            player.setYHeadRot(shipEntity.yRot);
        }
    }
}

package xyz.brassgoggledcoders.skyships.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

import java.util.function.Supplier;

public class UpdateSkyShipControlPacket {
    private final boolean left;
    private final boolean right;
    private final int vertical;

    public UpdateSkyShipControlPacket(boolean left, boolean right, int up) {
        this.left = left;
        this.right = right;
        this.vertical = up;
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBoolean(this.left);
        packetBuffer.writeBoolean(this.right);
        packetBuffer.writeInt(this.vertical);
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player != null && player.getVehicle() instanceof SkyShip) {
            ((SkyShip) player.getVehicle()).setPlayerPaddleState(
                    left,
                    right,
                    vertical
            );
        }
    }

    public static UpdateSkyShipControlPacket decode(FriendlyByteBuf packetBuffer) {
        return new UpdateSkyShipControlPacket(
                packetBuffer.readBoolean(),
                packetBuffer.readBoolean(),
                packetBuffer.readInt()
        );
    }


}

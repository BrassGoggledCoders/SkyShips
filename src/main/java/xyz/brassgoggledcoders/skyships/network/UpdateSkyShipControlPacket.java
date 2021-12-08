package xyz.brassgoggledcoders.skyships.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.skyships.entity.SkyShipEntity;

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

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(this.left);
        packetBuffer.writeBoolean(this.right);
        packetBuffer.writeInt(this.vertical);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = contextSupplier.get().getSender();
            if (player != null && player.getVehicle() instanceof SkyShipEntity) {
                ((SkyShipEntity) player.getVehicle()).setPaddleState(
                        left,
                        right,
                        vertical
                );
            }
        });
        return true;
    }

    public static UpdateSkyShipControlPacket decode(PacketBuffer packetBuffer) {
        return new UpdateSkyShipControlPacket(
                packetBuffer.readBoolean(),
                packetBuffer.readBoolean(),
                packetBuffer.readInt()
        );
    }


}

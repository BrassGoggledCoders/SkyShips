package xyz.brassgoggledcoders.skyships.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import xyz.brassgoggledcoders.skyships.SkyShips;

public class NetworkHandler {
    private final SimpleChannel channel;

    public NetworkHandler() {
        this.channel = NetworkRegistry.newSimpleChannel(
                SkyShips.rl("network"),
                () -> "1",
                "1"::equalsIgnoreCase,
                "1"::equalsIgnoreCase
        );

        this.channel.messageBuilder(UpdateSkyShipControlPacket.class, 0)
                .encoder(UpdateSkyShipControlPacket::encode)
                .decoder(UpdateSkyShipControlPacket::decode)
                .consumerMainThread(UpdateSkyShipControlPacket::consume)
                .add();
    }

    public void updateSkyShipControl(boolean left, boolean right, int vertical) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new UpdateSkyShipControlPacket(left, right, vertical));
    }
}

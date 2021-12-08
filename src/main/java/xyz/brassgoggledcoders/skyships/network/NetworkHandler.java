package xyz.brassgoggledcoders.skyships.network;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
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
                .consumer(UpdateSkyShipControlPacket::consume)
                .add();
    }

    public void updateSkyShipControl(boolean left, boolean right, int vertical) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new UpdateSkyShipControlPacket(left, right, vertical));
    }
}

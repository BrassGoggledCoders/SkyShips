package xyz.brassgoggledcoders.skyships.engine;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

public class ManualEngine extends Engine {
    public static final Codec<ManualEngine> CODEC = Codec.unit(ManualEngine::new);

    @Override
    public boolean tryRun(SkyShip skyShip) {
        return !skyShip.getPassengers().isEmpty() && skyShip.getPassengers().get(0) instanceof Player;
    }

    @Override
    @NotNull
    public Codec<? extends Engine> getCodec() {
        return CODEC;
    }
}

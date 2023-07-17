package xyz.brassgoggledcoders.skyships.engine;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

public abstract class Engine {

    public void tick(SkyShip skyShip) {

    }

    public abstract boolean canRun(SkyShip skyShip);

    @NotNull
    public abstract Codec<? extends Engine> getCodec();

    public void maneuver(SkyShip skyShip) {
    }
}

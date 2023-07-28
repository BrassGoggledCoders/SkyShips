package xyz.brassgoggledcoders.skyships.engine;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

@SuppressWarnings("unused")
public abstract class Engine {

    public void tick(SkyShip skyShip) {

    }

    public abstract boolean tryRun(SkyShip skyShip);

    @NotNull
    public abstract Codec<? extends Engine> getCodec();

    public float getSpeedModifier(SkyShip skyShip) {
        return 0.15F;
    }
}

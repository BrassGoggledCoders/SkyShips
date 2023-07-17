package xyz.brassgoggledcoders.skyships.engine;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

public class ManualEngine extends Engine {
    public static final Codec<ManualEngine> CODEC = Codec.unit(ManualEngine::new);

    @Override
    public boolean canRun(SkyShip skyShip) {
        return true;
    }

    @Override
    public void maneuver(SkyShip skyShip) {
        if (skyShip.getPassengers().isEmpty() || !(skyShip.getPassengers().get(0) instanceof Player)) {
            skyShip.setPaddleState(false, false, 0);
        }

        skyShip.floatBoat();
        if (skyShip.getLevel().isClientSide()) {
            skyShip.controlBoat();
            SkyShips.networkHandler.updateSkyShipControl(skyShip.getPaddleState(0), skyShip.getPaddleState(1), skyShip.getInputVertical());
        }
    }

    @Override
    @NotNull
    public Codec<? extends Engine> getCodec() {
        return CODEC;
    }
}

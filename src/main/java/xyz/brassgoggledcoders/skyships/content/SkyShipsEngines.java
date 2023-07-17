package xyz.brassgoggledcoders.skyships.content;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.engine.Engine;
import xyz.brassgoggledcoders.skyships.engine.ManualEngine;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class SkyShipsEngines {

    public static final ResourceKey<Registry<Codec<? extends Engine>>> RESOURCE_KEY = SkyShips.getRegistrate()
            .makeRegistry("engines", RegistryBuilder::new);

    public static final Codec<Engine> CODEC = ExtraCodecs.lazyInitializedCodec(
            () -> RegistryManager.ACTIVE.getRegistry(RESOURCE_KEY)
                    .getCodec()
                    .dispatch(
                            Engine::getCodec,
                            Function.identity()
                    )
    );

    public static final RegistryEntry<Codec<ManualEngine>> MANUAL = SkyShips.getRegistrate()
            .object("manual")
            .simple(RESOURCE_KEY, () -> ManualEngine.CODEC);

    public static void setup() {

    }
}

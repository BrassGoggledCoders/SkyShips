package xyz.brassgoggledcoders.skyships.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.AeroporteHook;

import javax.annotation.Nonnull;

public class AeroporteHookRenderer extends EntityRenderer<AeroporteHook> {
    private static final ResourceLocation TEXTURE = SkyShips.rl("hook");

    public AeroporteHookRenderer(EntityRendererProvider.Context rendererManager) {
        super(rendererManager);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull AeroporteHook pEntity) {
        return TEXTURE;
    }
}

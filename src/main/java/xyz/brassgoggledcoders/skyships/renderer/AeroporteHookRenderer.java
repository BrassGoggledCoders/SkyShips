package xyz.brassgoggledcoders.skyships.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.AeroporteHookEntity;

import javax.annotation.Nonnull;

public class AeroporteHookRenderer extends EntityRenderer<AeroporteHookEntity> {
    private static final ResourceLocation TEXTURE = SkyShips.rl("hook");

    public AeroporteHookRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull AeroporteHookEntity pEntity) {
        return TEXTURE;
    }
}

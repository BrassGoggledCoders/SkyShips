package xyz.brassgoggledcoders.skyships.compat.transport;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import xyz.brassgoggledcoders.skyships.renderer.SkyShipRenderer;

public class ShellSkyShipRenderer extends SkyShipRenderer<ShellSkyShip> {
    public ShellSkyShipRenderer(EntityRendererProvider.Context rendererManager) {
        super(rendererManager);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void renderContents(ShellSkyShip skyShip, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        Minecraft.getInstance()
                .getBlockRenderer()
                .renderSingleBlock(skyShip.getContent().getViewBlockState(), pMatrixStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY);
    }
}

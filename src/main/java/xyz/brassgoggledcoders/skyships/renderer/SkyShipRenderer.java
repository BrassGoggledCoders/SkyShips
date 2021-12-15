package xyz.brassgoggledcoders.skyships.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShipEntity;

import javax.annotation.Nonnull;

public class SkyShipRenderer extends EntityRenderer<SkyShipEntity> {
    private static final ResourceLocation BALLOON_TEXTURE = SkyShips.rl("textures/entity/sky_ship_balloon.png");
    private static final ResourceLocation STEERING_TEXTURE = SkyShips.rl("textures/entity/sky_ship_steering.png");
    private static final ResourceLocation GONDOLA_TEXTURE = SkyShips.rl("textures/entity/sky_ship_gondola.png");

    private final SkyShipSteeringModel<SkyShipEntity> steeringModel = new SkyShipSteeringModel<>();
    private final SkyShipBalloonModel<SkyShipEntity> balloonModel = new SkyShipBalloonModel<>();
    private final SkyShipGondolaModel<SkyShipEntity> gondolaModel = new SkyShipGondolaModel<>();

    public SkyShipRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(SkyShipEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, @Nonnull IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.0D, 0.375D, 0.0D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float) pEntity.getHurtTime() - pPartialTicks;
        float f1 = pEntity.getDamage() - pPartialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 10.0F * (float) pEntity.getHurtDir()));
        }

        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));

        pMatrixStack.translate(0F, -1.5F, 0F);
        this.balloonModel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder balloonVertexBuilder = pBuffer.getBuffer(this.balloonModel.renderType(BALLOON_TEXTURE));
        this.balloonModel.renderToBuffer(pMatrixStack, balloonVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        this.steeringModel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder gondolaVertexBuilder = pBuffer.getBuffer(this.steeringModel.renderType(STEERING_TEXTURE));
        this.steeringModel.renderToBuffer(pMatrixStack, gondolaVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        this.gondolaModel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder steeringVertexBuilder = pBuffer.getBuffer(this.gondolaModel.renderType(GONDOLA_TEXTURE));
        this.gondolaModel.renderToBuffer(pMatrixStack, steeringVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }


    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull SkyShipEntity pEntity) {
        return BALLOON_TEXTURE;
    }
}

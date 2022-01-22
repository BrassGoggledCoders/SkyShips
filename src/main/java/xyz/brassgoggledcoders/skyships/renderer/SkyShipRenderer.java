package xyz.brassgoggledcoders.skyships.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import xyz.brassgoggledcoders.skyships.SkyShips;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

import javax.annotation.Nonnull;

public class SkyShipRenderer extends EntityRenderer<SkyShip> {
    private static final ResourceLocation BALLOON_TEXTURE = SkyShips.rl("textures/entity/balloon_white.png");
    private static final ResourceLocation STEERING_TEXTURE = SkyShips.rl("textures/entity/steering_oak.png");
    private static final ResourceLocation GONDOLA_TEXTURE = SkyShips.rl("textures/entity/gondola_oak.png");

    public static final ModelLayerLocation BALLOON_LOCATION = new ModelLayerLocation(SkyShips.rl("balloon/white"), "main");
    public static final ModelLayerLocation STEERING_LOCATION = new ModelLayerLocation(SkyShips.rl("steering/oak"), "main");
    public static final ModelLayerLocation GONDOLA_LOCATION = new ModelLayerLocation(SkyShips.rl("gondola/oak"), "main");

    private final BalloonModel<SkyShip> balloonModel;
    private final SteeringModel<SkyShip> steeringModel;
    private final GondolaModel<SkyShip> gondolaModel;

    public SkyShipRenderer(EntityRendererProvider.Context rendererManager) {
        super(rendererManager);
        this.balloonModel = new BalloonModel<>(rendererManager.bakeLayer(BALLOON_LOCATION));
        this.steeringModel = new SteeringModel<>(rendererManager.bakeLayer(STEERING_LOCATION));
        this.gondolaModel = new GondolaModel<>(rendererManager.bakeLayer(GONDOLA_LOCATION));
    }

    @Override
    public void render(SkyShip pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, @Nonnull MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.0D, 0.375D, 0.0D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float) pEntity.getHurtTime() - pPartialTicks;
        float f1 = pEntity.getDamage() - pPartialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float) pEntity.getHurtDir()));
        }

        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));

        pMatrixStack.translate(0F, -1.5F, 0F);
        this.balloonModel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer balloonVertexBuilder = pBuffer.getBuffer(this.balloonModel.renderType(BALLOON_TEXTURE));
        this.balloonModel.renderToBuffer(pMatrixStack, balloonVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        this.steeringModel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer gondolaVertexBuilder = pBuffer.getBuffer(this.steeringModel.renderType(STEERING_TEXTURE));
        this.steeringModel.renderToBuffer(pMatrixStack, gondolaVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        this.gondolaModel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer steeringVertexBuilder = pBuffer.getBuffer(this.gondolaModel.renderType(GONDOLA_TEXTURE));
        this.gondolaModel.renderToBuffer(pMatrixStack, steeringVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }


    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull SkyShip pEntity) {
        return BALLOON_TEXTURE;
    }
}

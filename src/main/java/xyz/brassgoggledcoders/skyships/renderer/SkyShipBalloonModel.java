package xyz.brassgoggledcoders.skyships.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("FieldCanBeLocal")
public class SkyShipBalloonModel<T extends SkyShip> extends EntityModel<T> {
    private final ModelPart balloon;
    private final ModelPart cube_r1;
    private final ModelPart cube_r2;

    public SkyShipBalloonModel() {
        texWidth = 128;
        texHeight = 128;

        balloon = new ModelPart(this);
        balloon.setPos(0.0F, 24.0F, 0.0F);
        balloon.texOffs(0, 64).addBox(-16.0F, -57.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, false);
        balloon.texOffs(0, 0).addBox(-16.0F, -57.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.25F, false);

        cube_r1 = new ModelPart(this);
        cube_r1.setPos(-15.75F, -12.6759F, -13.0148F);
        balloon.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.2618F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(24.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.0F, 0.0F, false);
        cube_r1.texOffs(0, 0).addBox(5.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.0F, 0.0F, false);

        cube_r2 = new ModelPart(this);
        cube_r2.setPos(3.25F, -12.6762F, 13.0138F);
        balloon.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.2618F, 0.0F, 0.0F);
        cube_r2.texOffs(0, 0).addBox(5.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.0F, 0.0F, false);
        cube_r2.texOffs(0, 0).addBox(-13.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        balloon.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}

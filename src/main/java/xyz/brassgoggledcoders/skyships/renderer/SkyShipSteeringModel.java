package xyz.brassgoggledcoders.skyships.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import xyz.brassgoggledcoders.skyships.entity.SkyShipEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("FieldCanBeLocal")
public class SkyShipSteeringModel<T extends SkyShipEntity> extends EntityModel<T> {
    private final ModelPart addons;
    private final ModelPart cube_r1;
    private final ModelPart cube_r2;
    private final ModelPart cube_r3;
    private final ModelPart cube_r4;
    private final ModelPart cube_r5;

    public SkyShipSteeringModel() {
        texWidth = 64;
        texHeight = 64;

        addons = new ModelPart(this);
        addons.setPos(0.0F, 24.0F, 0.0F);


        cube_r1 = new ModelPart(this);
        cube_r1.setPos(0.0F, -3.0F, 12.0F);
        addons.addChild(cube_r1);
        setRotationAngle(cube_r1, 2.5307F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 6).addBox(-3.0F, -6.0F, -15.0F, 0.0F, 12.0F, 12.0F, 0.0F, false);
        cube_r1.texOffs(0, 0).addBox(-4.0F, -1.0F, -10.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r2 = new ModelPart(this);
        cube_r2.setPos(0.0F, -3.0F, 12.0F);
        addons.addChild(cube_r2);
        setRotationAngle(cube_r2, 2.5307F, 0.0F, 0.0F);
        cube_r2.texOffs(0, 0).addBox(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 14.0F, 0.0F, false);

        cube_r3 = new ModelPart(this);
        cube_r3.setPos(20.7291F, -0.4851F, 0.0F);
        addons.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.0F);
        cube_r3.texOffs(22, 0).addBox(3.5F, -6.0F, 0.0F, 8.0F, 12.0F, 0.0F, 0.01F, false);

        cube_r4 = new ModelPart(this);
        cube_r4.setPos(0.0F, -3.0F, -12.0F);
        addons.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.6109F, 0.0F, 0.0F);
        cube_r4.texOffs(0, 0).addBox(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 14.0F, 0.0F, false);

        cube_r5 = new ModelPart(this);
        cube_r5.setPos(0.0F, -3.0F, -12.0F);
        addons.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.6109F, 0.0F, 0.0F);
        cube_r5.texOffs(0, 6).addBox(-3.0F, -6.0F, -15.0F, 0.0F, 12.0F, 12.0F, 0.0F, false);
        cube_r5.texOffs(0, 0).addBox(-4.0F, -1.0F, -10.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        addons.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
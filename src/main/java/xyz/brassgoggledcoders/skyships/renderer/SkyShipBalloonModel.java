package xyz.brassgoggledcoders.skyships.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import xyz.brassgoggledcoders.skyships.entity.SkyShipEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("FieldCanBeLocal")
public class SkyShipBalloonModel<T extends SkyShipEntity> extends EntityModel<T> {
    private final ModelRenderer balloon;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;

    public SkyShipBalloonModel() {
        texWidth = 128;
        texHeight = 128;

        balloon = new ModelRenderer(this);
        balloon.setPos(0.0F, 24.0F, 0.0F);
        balloon.texOffs(0, 64).addBox(-16.0F, -57.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, false);
        balloon.texOffs(0, 0).addBox(-16.0F, -57.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.25F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-15.75F, -12.6759F, -13.0148F);
        balloon.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.2618F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(24.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.0F, 0.0F, false);
        cube_r1.texOffs(0, 0).addBox(5.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
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
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        balloon.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}

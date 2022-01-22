package xyz.brassgoggledcoders.skyships.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import xyz.brassgoggledcoders.skyships.entity.SkyShipEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SkyShipGondolaModel<T extends SkyShipEntity> extends EntityModel<T> {
    private final ModelPart boat;
    private final ModelPart cube_r1;
    private final ModelPart cube_r2;
    private final ModelPart cube_r3;
    private final ModelPart cube_r4;
    private final ModelPart cube_r5;
    private final ModelPart cube_r6;
    private final ModelPart cube_r7;
    private final ModelPart cube_r8;
    private final ModelPart cube_r9;
    private final ModelPart cube_r10;
    private final ModelPart cube_r11;
    private final ModelPart cube_r12;
    private final ModelPart cube_r13;

    public SkyShipGondolaModel() {
        texWidth = 128;
        texHeight = 128;

        boat = new ModelPart(this);
        boat.setPos(0.0F, 24.0F, 0.0F);
        boat.texOffs(0, 23).addBox(-14.0F, -6.0F, 8.0F, 28.0F, 6.0F, 4.0F, 0.001F, false);
        boat.texOffs(40, 33).addBox(17.1406F, -6.0F, -6.5203F, 4.0F, 6.0F, 13.0F, 0.0F, false);
        boat.texOffs(24, 33).addBox(17.1406F, 0.0F, -5.5203F, 2.0F, 2.0F, 11.0F, 0.0F, false);
        boat.texOffs(0, 23).addBox(-14.0F, -6.0F, -12.0F, 28.0F, 6.0F, 4.0F, 0.0F, false);

        cube_r1 = new ModelPart(this);
        cube_r1.setPos(0.0F, 0.0F, 9.0F);
        boat.addChild(cube_r1);
        setRotationAngle(cube_r1, -1.0472F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 11).addBox(-14.0F, 0.0F, -2.0F, 31.0F, 10.0F, 2.0F, 0.0F, false);

        cube_r2 = new ModelPart(this);
        cube_r2.setPos(-13.2912F, 0.1139F, 9.1664F);
        boat.addChild(cube_r2);
        setRotationAngle(cube_r2, -1.1056F, -0.3294F, 0.441F);
        cube_r2.texOffs(0, 55).addBox(-14.74F, 0.4828F, -1.8631F, 14.0F, 7.0F, 2.0F, 0.0F, false);

        cube_r3 = new ModelPart(this);
        cube_r3.setPos(-13.3601F, -5.4559F, -9.3485F);
        boat.addChild(cube_r3);
        setRotationAngle(cube_r3, 1.0977F, 0.3136F, 0.4385F);
        cube_r3.texOffs(0, 55).addBox(-12.4718F, 3.4287F, -4.1806F, 14.0F, 7.0F, 2.0F, 0.0F, false);

        cube_r4 = new ModelPart(this);
        cube_r4.setPos(0.0F, -1.7321F, -8.0F);
        boat.addChild(cube_r4);
        setRotationAngle(cube_r4, 1.0472F, 0.0F, 0.0F);
        cube_r4.texOffs(0, 11).addBox(-14.0F, 0.0F, -2.0F, 31.0F, 10.0F, 2.0F, 0.0F, false);

        cube_r5 = new ModelPart(this);
        cube_r5.setPos(-13.4775F, 0.3527F, -8.6251F);
        boat.addChild(cube_r5);
        setRotationAngle(cube_r5, -3.1416F, 1.0036F, -2.8798F);
        cube_r5.texOffs(0, 33).addBox(0.0F, -6.0F, -16.0F, 4.0F, 6.0F, 16.0F, 0.001F, false);

        cube_r6 = new ModelPart(this);
        cube_r6.setPos(-13.4785F, 0.3517F, 8.6261F);
        boat.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 1.0036F, 0.2618F);
        cube_r6.texOffs(0, 33).addBox(-4.0F, -6.0F, -16.0F, 4.0F, 6.0F, 16.0F, 0.001F, false);

        cube_r7 = new ModelPart(this);
        cube_r7.setPos(20.3248F, 0.0F, 5.119F);
        boat.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, -0.9163F, 0.0F);
        cube_r7.texOffs(61, 24).addBox(-2.3912F, -6.0F, 0.2066F, 4.0F, 6.0F, 9.0F, 0.01F, false);

        cube_r8 = new ModelPart(this);
        cube_r8.setPos(12.8567F, 0.0F, -10.8495F);
        boat.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.9163F, 0.0F);
        cube_r8.texOffs(61, 24).addBox(-2.3912F, -6.0F, 0.2066F, 4.0F, 6.0F, 9.0F, 0.01F, false);

        cube_r9 = new ModelPart(this);
        cube_r9.setPos(20.7291F, -0.4851F, 0.0F);
        boat.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, 0.0F);
        cube_r9.texOffs(61, 39).addBox(-3.5F, -7.0F, -2.0F, 12.0F, 2.0F, 4.0F, 0.01F, false);

        cube_r10 = new ModelPart(this);
        cube_r10.setPos(20.7291F, -0.4851F, 0.0F);
        boat.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, 0.3491F);
        cube_r10.texOffs(0, 64).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 12.0F, 6.0F, 0.01F, false);

        cube_r11 = new ModelPart(this);
        cube_r11.setPos(-26.5069F, -6.4365F, 0.0F);
        boat.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, -0.2618F);
        cube_r11.texOffs(0, 64).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 12.0F, 6.0F, 0.0F, false);

        cube_r12 = new ModelPart(this);
        cube_r12.setPos(-10.9748F, 3.5071F, 0.0F);
        boat.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.0F, 0.0F);
        cube_r12.texOffs(0, 0).addBox(-6.0F, -2.5F, -3.0F, 38.0F, 5.0F, 6.0F, 0.0F, false);

        cube_r13 = new ModelPart(this);
        cube_r13.setPos(-26.7169F, -0.7122F, 0.001F);
        boat.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.0F, 0.5236F);
        cube_r13.texOffs(34, 52).addBox(-0.2044F, -5.0529F, -3.001F, 12.0F, 6.0F, 6.0F, 0.001F, false);
    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        boat.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}

package xyz.brassgoggledcoders.skyships.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
public class GondolaModel<T extends SkyShip> extends EntityModel<T> {
    private final ModelPart boat;

    public GondolaModel(ModelPart root) {
        this.boat = root.getChild("boat");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition boat = partdefinition.addOrReplaceChild("boat", CubeListBuilder.create().texOffs(0, 23).addBox(-14.0F, -6.0F, 8.0F, 28.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
                .texOffs(40, 33).addBox(17.1406F, -6.0F, -6.5203F, 4.0F, 6.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(24, 33).addBox(17.1406F, 0.0F, -5.5203F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 23).addBox(-14.0F, -6.0F, -12.0F, 28.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = boat.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-14.0F, 0.0F, -2.0F, 31.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition cube_r2 = boat.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(61, 33).addBox(-14.74F, 0.4828F, -1.8631F, 14.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.2912F, 0.1139F, 9.1664F, -1.1056F, -0.3294F, 0.441F));

        PartDefinition cube_r3 = boat.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(61, 33).addBox(-12.4718F, 3.4287F, -4.1806F, 14.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.3601F, -5.4559F, -9.3485F, 1.0977F, 0.3136F, 0.4385F));

        PartDefinition cube_r4 = boat.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 11).addBox(-14.0F, 0.0F, -2.0F, 31.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.7321F, -8.0F, 1.0472F, 0.0F, 0.0F));

        PartDefinition cube_r5 = boat.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 33).addBox(0.0F, -6.0F, -16.0F, 4.0F, 6.0F, 16.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-13.4775F, 0.3527F, -8.6251F, -3.1416F, 1.0036F, -2.8798F));

        PartDefinition cube_r6 = boat.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 33).addBox(-4.0F, -6.0F, -16.0F, 4.0F, 6.0F, 16.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-13.4785F, 0.3517F, 8.6261F, 0.0F, 1.0036F, 0.2618F));

        PartDefinition cube_r7 = boat.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 55).addBox(-2.3912F, -6.0F, 0.2066F, 4.0F, 6.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(20.3248F, 0.0F, 5.119F, 0.0F, -0.9163F, 0.0F));

        PartDefinition cube_r8 = boat.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 55).addBox(-2.3912F, -6.0F, 0.2066F, 4.0F, 6.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(12.8567F, 0.0F, -10.8495F, 0.0F, 0.9163F, 0.0F));

        PartDefinition cube_r9 = boat.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(62, 19).addBox(-3.5F, -7.0F, -2.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(20.7291F, -0.4851F, 0.0F));

        PartDefinition cube_r10 = boat.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(20, 64).mirror().addBox(-2.5F, -6.0F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(20.7291F, -0.4851F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition cube_r11 = boat.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(20, 64).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-26.5069F, -6.4365F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition cube_r12 = boat.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.5F, -3.0F, 38.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.9748F, 3.5071F, 0.0F));

        PartDefinition cube_r13 = boat.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(34, 52).addBox(-0.2044F, -5.0529F, -3.001F, 12.0F, 6.0F, 6.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-26.7169F, -0.7122F, 0.001F, 0.0F, 0.0F, 0.5236F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        boat.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
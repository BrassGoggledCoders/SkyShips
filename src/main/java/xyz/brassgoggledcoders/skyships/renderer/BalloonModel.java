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
public class BalloonModel<T extends SkyShip> extends EntityModel<T> {
    private final ModelPart balloon;

    public BalloonModel(ModelPart root) {
        this.balloon = root.getChild("balloon");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition balloon = partdefinition.addOrReplaceChild("balloon", CubeListBuilder.create().texOffs(0, 64).addBox(-16.0F, -57.0F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-16.0F, -57.0F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leftLines = balloon.addOrReplaceChild("leftLines", CubeListBuilder.create().texOffs(12, 0).addBox(23.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.01F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(5.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.01F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.75F, -12.6759F, -13.0148F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rightLines = balloon.addOrReplaceChild("rightLines", CubeListBuilder.create().texOffs(4, 0).addBox(4.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.01F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-13.5F, -12.5F, 0.0F, 2.0F, 25.0F, 0.01F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.25F, -12.6762F, 13.0138F, -0.2618F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        balloon.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
package xyz.brassgoggledcoders.skyships.renderer;// Made with Blockbench 4.1.1
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

@SuppressWarnings("unused")
public class SteeringModel<T extends SkyShip> extends EntityModel<T> {
    private final ModelPart addons;
    private final ModelPart leftPropeller;
    private final ModelPart rightPropeller;

    public SteeringModel(ModelPart root) {
        this.addons = root.getChild("addons");
        this.leftPropeller = this.addons.getChild("leftPropeller");
        this.rightPropeller = this.addons.getChild("rightPropeller");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition addons = partdefinition.addOrReplaceChild("addons", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rightPropeller = addons.addOrReplaceChild("rightPropeller", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-3.0F, -6.0F, -6.0F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).addBox(-4.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 18.0F, 2.5307F, 0.0F, 0.0F));

        //Propeller Supports
        PartDefinition cube_r2 = addons.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 12.0F, 2.5307F, 0.0F, 0.0F));
        PartDefinition cube_r3 = addons.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 0).addBox(3.5F, -6.0F, 0.0F, 8.0F, 12.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(20.7291F, -0.4851F, 0.0F));
        PartDefinition cube_r4 = addons.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -12.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition leftPropeller = addons.addOrReplaceChild("leftPropeller", CubeListBuilder.create().texOffs(0, 6).addBox(-3.0F, -6.0F, -6.0F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -18.0F, 0.6109F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	this.leftPropeller.xRot = entity.level.getGameTime() % 360;
    	this.rightPropeller.xRot = -entity.level.getGameTime() % 360;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        addons.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
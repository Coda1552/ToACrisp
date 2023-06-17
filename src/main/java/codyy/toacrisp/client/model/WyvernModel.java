package codyy.toacrisp.client.model;

import codyy.toacrisp.common.entities.Wyvern;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WyvernModel<T extends Wyvern> extends EntityModel<T> {
	private final ModelPart bone;

	public WyvernModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -20.0F, -3.0F, 13.0F, 13.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(46, 28).addBox(-4.5F, -21.0F, -1.0F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(46, 31).addBox(0.5F, -21.0F, -3.0F, 0.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -20.0F, 10.0F, 7.0F, 7.0F, 16.0F, new CubeDeformation(-0.1F))
		.texOffs(52, 51).addBox(0.5F, -21.0F, 12.0F, 0.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Lleg = body.addOrReplaceChild("Lleg", CubeListBuilder.create(), PartPose.offset(6.0F, -12.0F, 9.0F));

		PartDefinition thigh = Lleg.addOrReplaceChild("thigh", CubeListBuilder.create().texOffs(64, 5).addBox(-2.5F, -2.5F, -4.5F, 5.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -0.5F, -0.5F));

		PartDefinition Lleg2 = thigh.addOrReplaceChild("Lleg2", CubeListBuilder.create().texOffs(48, 76).addBox(5.0F, -9.0F, 9.0F, 4.0F, 9.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offset(-7.0F, 12.5F, -8.5F));

		PartDefinition foot = Lleg2.addOrReplaceChild("foot", CubeListBuilder.create().texOffs(30, 36).addBox(4.5F, -3.0F, 5.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 73).addBox(4.5F, -4.0F, 7.0F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Rleg = body.addOrReplaceChild("Rleg", CubeListBuilder.create(), PartPose.offset(-3.0F, -12.0F, 9.0F));

		PartDefinition thigh2 = Rleg.addOrReplaceChild("thigh2", CubeListBuilder.create().texOffs(52, 47).addBox(-2.5F, -2.5F, -4.5F, 5.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -0.5F, -0.5F));

		PartDefinition Rleg2 = thigh2.addOrReplaceChild("Rleg2", CubeListBuilder.create().texOffs(75, 23).addBox(5.0F, -9.0F, 9.0F, 4.0F, 9.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offset(-7.0F, 12.5F, -8.5F));

		PartDefinition foot2 = Rleg2.addOrReplaceChild("foot2", CubeListBuilder.create().texOffs(0, 34).addBox(4.5F, -3.0F, 5.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 73).addBox(4.5F, -4.0F, 7.0F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Lwing = body.addOrReplaceChild("Lwing", CubeListBuilder.create().texOffs(8, 84).addBox(-1.0F, -21.25F, -1.25F, 2.0F, 21.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 38).addBox(0.0F, -22.25F, -4.25F, 0.0F, 22.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -19.75F, 1.25F, 0.0F, 0.0F, 2.7489F));

		PartDefinition Rwing = body.addOrReplaceChild("Rwing", CubeListBuilder.create().texOffs(0, 84).addBox(-1.0F, -21.25F, -1.25F, 2.0F, 21.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(0.0F, -22.25F, -4.25F, 0.0F, 22.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -19.75F, 1.25F, 0.0F, 0.0F, -2.7489F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(70, 70).addBox(-3.0F, -9.0F, -7.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(-0.025F)), PartPose.offset(0.5F, -11.0F, -2.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -8.0F, -3.0F));

		PartDefinition topjaw = head.addOrReplaceChild("topjaw", CubeListBuilder.create().texOffs(41, 0).addBox(-4.0F, -6.25F, -6.75F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-2.5F, -4.25F, -8.75F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.025F))
		.texOffs(83, 0).addBox(-2.5F, -5.25F, -13.75F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.025F))
		.texOffs(0, 0).addBox(0.0F, -6.25F, -12.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 28).addBox(-2.5F, -0.25F, -13.75F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.025F)), PartPose.offset(0.5F, -1.75F, 0.75F));

		PartDefinition Rhorn = topjaw.addOrReplaceChild("Rhorn", CubeListBuilder.create().texOffs(0, 7).addBox(-1.0F, -0.75F, -0.25F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 39).addBox(-1.0F, -3.75F, 2.75F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -5.5F, -0.5F, 0.3927F, -0.3927F, 0.0F));

		PartDefinition Lhorn = topjaw.addOrReplaceChild("Lhorn", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.5F, -0.25F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(41, 0).addBox(-1.0F, -5.5F, 2.75F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -4.75F, -0.5F, 0.3927F, 0.3927F, 0.0F));

		PartDefinition bottomjaw = head.addOrReplaceChild("bottomjaw", CubeListBuilder.create().texOffs(44, 66).addBox(-4.0F, -0.5F, -7.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(73, 59).addBox(-2.0F, -0.5F, -14.0F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(16, 84).addBox(-2.0F, -1.5F, -14.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(71, 46).addBox(-3.0F, -4.5F, -5.5F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.5F, 1.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
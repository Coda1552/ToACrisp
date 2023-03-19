package coda.toacrisp.client.model;

import coda.toacrisp.common.entities.Cockatrice;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class CockatriceModel<T extends Cockatrice> extends AgeableListModel<T> {
	private final ModelPart bone;
	private final ModelPart body;
	private final ModelPart wholebody;
	private final ModelPart head;
	private final ModelPart rightwing;
	private final ModelPart leftwing;
	private final ModelPart rightleg;
	private final ModelPart leftleg;
	private final ModelPart tail;
	private final ModelPart tailsegment;

	public CockatriceModel(ModelPart root) {
		this.bone = root.getChild("bone");

		this.body = bone.getChild("body");
		this.wholebody = body.getChild("wholebody");
		this.head = bone.getChild("head");
		this.rightwing = body.getChild("rightwing");
		this.leftwing = body.getChild("leftwing");
		this.rightleg = body.getChild("rightleg");
		this.leftleg = body.getChild("leftleg");
		this.tail = body.getChild("tail");
		this.tailsegment = tail.getChild("tailsegment");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 1.0F));

		PartDefinition wholebody = body.addOrReplaceChild("wholebody", CubeListBuilder.create().texOffs(28, 2).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 4).addBox(0.25F, -11.25F, -3.25F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(20, 35).addBox(-1.75F, -8.25F, -3.25F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-0.25F, -7.25F, -6.25F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(5, 0).addBox(-0.25F, -5.25F, -6.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -0.75F, -1.75F));

		PartDefinition rightwing = body.addOrReplaceChild("rightwing", CubeListBuilder.create().texOffs(0, 23).addBox(0.5F, 0.0F, -4.0F, 13.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -2.0F, -3.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leftwing = body.addOrReplaceChild("leftwing", CubeListBuilder.create().texOffs(0, 0).addBox(-13.5F, 0.0F, -4.0F, 13.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -2.0F, -3.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -2.25F, 2.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 33).addBox(-1.5F, -0.25F, 0.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.75F, 3.5F));

		PartDefinition tailsegment = tail.addOrReplaceChild("tailsegment", CubeListBuilder.create().texOffs(25, 22).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.75F, 7.5F));

		PartDefinition rightleg = body.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(40, 16).addBox(-0.5F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 3.0F, 2.0F));

		PartDefinition leftleg = body.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(36, 35).addBox(-2.5F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 3.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);

		// temporary definitions so I can test animations
		//limbSwing = ageInTicks;
		//limbSwingAmount = 0.4F;
		this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

		this.head.xRot += Mth.cos(1.0F + limbSwing * 0.5F) * 0.25F * limbSwingAmount;
		this.head.y = Mth.cos(limbSwing * 0.5F) * 1.5F * limbSwingAmount;

		this.tail.xRot = Mth.cos(1.0F + limbSwing * 0.5F) * 0.3F * limbSwingAmount - 0.2F;
		this.tailsegment.xRot = Mth.cos(-1.0F + limbSwing * 0.5F) * 0.45F * limbSwingAmount + 0.1F;

		this.leftwing.zRot = Mth.cos(-1.0F + limbSwing * 0.8F) * 0.8F * limbSwingAmount + 0.5F;
		this.rightwing.zRot = -Mth.cos(-1.0F + limbSwing * 0.8F) * 0.8F * limbSwingAmount - 0.5F;

		float shift = 15F;

		this.wholebody.y = shift + 1F;
		this.tail.y = shift - 1F;
		this.leftwing.y = shift + 1F;
		this.rightwing.y = shift + 1F;
		this.leftleg.y = shift + 4F;
		this.rightleg.y = shift + 4F;
		this.head.y = shift;
	}

	// todo - sitting pose, including tail animation while its sitting
	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (young) {
			pPoseStack.pushPose();
			pPoseStack.scale(0.7F, 0.7F, 0.7F);
			pPoseStack.translate(0F, 0.85F, 0F);
			headParts().forEach(a -> a.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay));
			pPoseStack.popPose();

			pPoseStack.pushPose();
			pPoseStack.scale(0.5F, 0.5F, 0.5F);
			pPoseStack.translate(0F, 1.5F, 0F);
			bodyParts().forEach(a -> a.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay));
			pPoseStack.popPose();
		}
		else {
			bodyParts().forEach(a -> a.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay));
			headParts().forEach(a -> a.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay));
		}
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(wholebody, tail, leftwing, rightwing, leftleg, rightleg);
	}
}
package coda.toacrisp.client.render;

import coda.toacrisp.ToACrisp;
import coda.toacrisp.client.TACModelLayers;
import coda.toacrisp.client.model.CockatriceModel;
import coda.toacrisp.common.entities.Cockatrice;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CockatriceRenderer extends MobRenderer<Cockatrice, CockatriceModel<Cockatrice>> {
    private static final ResourceLocation LOC = new ResourceLocation(ToACrisp.MOD_ID, "textures/entity/cockatrice/cockatrice.png");
    private static final ResourceLocation BEAM_LOCATION = new ResourceLocation(ToACrisp.MOD_ID, "textures/entity/cockatrice/beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityTranslucent(BEAM_LOCATION);

    public CockatriceRenderer(EntityRendererProvider.Context p_173952_) {
        super(p_173952_, new CockatriceModel<>(p_173952_.bakeLayer(TACModelLayers.COCKATRICE)), 0.3F);
    }

    public ResourceLocation getTextureLocation(Cockatrice p_113998_) {
        return LOC;
    }

    public boolean shouldRender(Cockatrice p_114836_, Frustum p_114837_, double p_114838_, double p_114839_, double p_114840_) {
        if (super.shouldRender(p_114836_, p_114837_, p_114838_, p_114839_, p_114840_)) {
            return true;
        } else {
            if (p_114836_.hasActiveAttackTarget()) {
                LivingEntity livingentity = p_114836_.getActiveAttackTarget();
                if (livingentity != null) {
                    Vec3 vec3 = this.getPosition(livingentity, (double)livingentity.getBbHeight() * 0.5D, 1.0F);
                    Vec3 vec31 = this.getPosition(p_114836_, p_114836_.getEyeHeight(), 1.0F);
                    return p_114837_.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
                }
            }

            return false;
        }
    }

    private Vec3 getPosition(LivingEntity p_114803_, double p_114804_, float p_114805_) {
        double d0 = Mth.lerp(p_114805_, p_114803_.xOld, p_114803_.getX());
        double d1 = Mth.lerp(p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
        double d2 = Mth.lerp(p_114805_, p_114803_.zOld, p_114803_.getZ());
        return new Vec3(d0, d1, d2);
    }

    public void render(Cockatrice cockatrice, float p_114830_, float p_114831_, PoseStack stack, MultiBufferSource buffer, int p_114834_) {
        super.render(cockatrice, p_114830_, p_114831_, stack, buffer, p_114834_);
        LivingEntity target = cockatrice.getActiveAttackTarget();
        if (target != null && target.isAlive()) {
            if (cockatrice.getActiveAttackTarget() != target) {
                return;
            }
            float f = cockatrice.getAttackAnimationScale(p_114831_);
            float f1 = (float)cockatrice.level.getGameTime() + p_114831_;
            float f2 = f1 * 0.5F % 1.0F;
            float f3 = cockatrice.getEyeHeight();
            stack.pushPose();
            stack.translate(0.0D, f3, 0.0D);
            Vec3 vec3 = this.getPosition(target, (double)target.getBbHeight() * 0.5D, p_114831_);
            Vec3 vec31 = this.getPosition(cockatrice, f3, p_114831_);
            Vec3 vec32 = vec3.subtract(vec31);
            float f4 = (float)(vec32.length() + 1.0D);
            vec32 = vec32.normalize();
            float f5 = (float)Math.acos(vec32.y);
            float f6 = (float)Math.atan2(vec32.z, vec32.x);
            stack.mulPose(Vector3f.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
            stack.mulPose(Vector3f.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
            float f7 = f1 * 0.05F * -1.5F;
            float f8 = f * f;
            float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
            float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
            float f13 = Mth.cos(f7 + ((float)Math.PI / 4F)) * 0.282F;
            float f14 = Mth.sin(f7 + ((float)Math.PI / 4F)) * 0.282F;
            float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
            float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
            float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
            float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
            float f19 = Mth.cos(f7 + (float)Math.PI) * 0.2F;
            float f20 = Mth.sin(f7 + (float)Math.PI) * 0.2F;
            float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
            float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
            float f23 = Mth.cos(f7 + ((float)Math.PI / 2F)) * 0.2F;
            float f24 = Mth.sin(f7 + ((float)Math.PI / 2F)) * 0.2F;
            float f25 = Mth.cos(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
            float f26 = Mth.sin(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
            float f29 = -1.0F + f2;
            float f30 = f4 * 2.5F + f29;
            VertexConsumer vertexconsumer = buffer.getBuffer(BEAM_RENDER_TYPE);
            PoseStack.Pose posestack$pose = stack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, 0.0F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, 0.0F, f30);
            float f31 = 0.0F;

            if (cockatrice.tickCount % 2 == 0) {
                f31 = 0.5F;
            }

            vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, 0.5F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, 1.0F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, 1.0F, f31);
            vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, 0.5F, f31);
            stack.popPose();
        }

    }

    private static void vertex(VertexConsumer p_114842_, Matrix4f p_114843_, Matrix3f p_114844_, float p_114845_, float p_114846_, float p_114847_, float p_114851_, float p_114852_) {
        p_114842_.vertex(p_114843_, p_114845_, p_114846_, p_114847_).color(255, 255, 255, 200).uv(p_114851_, p_114852_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_114844_, 0.0F, 1.0F, 0.0F).endVertex();
    }
}

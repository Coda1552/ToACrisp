package coda.toacrisp.client.render;

import coda.toacrisp.client.model.CockatriceModel;
import coda.toacrisp.common.entities.CockatriceEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CockatriceRenderer extends GeoEntityRenderer<CockatriceEntity> {

    public CockatriceRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CockatriceModel());
    }

    @Override
    public RenderType getRenderType(CockatriceEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }
}
package coda.toacrisp.client.render;

import coda.toacrisp.ToACrisp;
import coda.toacrisp.client.WModelLayers;
import coda.toacrisp.client.model.WyvernModel;
import coda.toacrisp.common.entities.Wyvern;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WyvernRenderer extends MobRenderer<Wyvern, WyvernModel<Wyvern>> {
    private static final ResourceLocation TEX = new ResourceLocation(ToACrisp.MOD_ID, "textures/entity/wyvern/wyvern.png");
    private static final ResourceLocation SADDLE_TEX = new ResourceLocation(ToACrisp.MOD_ID, "textures/entity/wyvern/saddle.png");

    public WyvernRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WyvernModel<>(pContext.bakeLayer(WModelLayers.WYVERN)), 1.0F);
        addLayer(new SaddleLayer<>(this, getModel(), SADDLE_TEX));
    }

    @Override
    public ResourceLocation getTextureLocation(Wyvern pEntity) {
        return TEX;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(Wyvern pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
        return RenderType.entityTranslucent(getTextureLocation(pLivingEntity));
    }

}

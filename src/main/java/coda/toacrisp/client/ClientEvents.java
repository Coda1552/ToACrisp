package coda.toacrisp.client;

import coda.toacrisp.ToACrisp;
import coda.toacrisp.client.model.CockatriceModel;
import coda.toacrisp.client.model.WyvernModel;
import coda.toacrisp.client.render.CockatriceRenderer;
import coda.toacrisp.client.render.WyvernRenderer;
import coda.toacrisp.registry.TACEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ToACrisp.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent e) {
        EntityRenderers.register(TACEntities.WYVERN.get(), WyvernRenderer::new);
        EntityRenderers.register(TACEntities.COCKATRICE.get(), CockatriceRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(TACModelLayers.WYVERN, WyvernModel::createBodyLayer);
        e.registerLayerDefinition(TACModelLayers.COCKATRICE, CockatriceModel::createBodyLayer);
    }

}

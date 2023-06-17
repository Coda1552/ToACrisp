package codyy.toacrisp.client;

import codyy.toacrisp.ToACrisp;
import codyy.toacrisp.client.model.CockatriceModel;
import codyy.toacrisp.client.model.WyvernModel;
import codyy.toacrisp.client.render.CockatriceRenderer;
import codyy.toacrisp.client.render.WyvernRenderer;
import codyy.toacrisp.registry.TACEntities;
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

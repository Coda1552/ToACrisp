package coda.toacrisp.client;

import coda.toacrisp.ToACrisp;
import coda.toacrisp.client.render.CockatriceRenderer;
import coda.toacrisp.registry.TACEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ToACrisp.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(TACEntities.COCKATRICE.get(), CockatriceRenderer::new);
    }
}

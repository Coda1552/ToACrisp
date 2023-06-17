package codyy.toacrisp;

import codyy.toacrisp.common.entities.Cockatrice;
import codyy.toacrisp.common.entities.Wyvern;
import codyy.toacrisp.registry.TACBlocks;
import codyy.toacrisp.registry.TACEntities;
import codyy.toacrisp.registry.TACItems;
import codyy.toacrisp.registry.TACParticles;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ToACrisp.MOD_ID)
public class ToACrisp {
    public static final String MOD_ID = "toacrisp";

    public ToACrisp() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        TACItems.ITEMS.register(bus);
        TACParticles.PARTICLES.register(bus);
        TACBlocks.BLOCKS.register(bus);
        TACEntities.ENTITIES.register(bus);

        bus.addListener(this::createAttributes);
    }

    private void createAttributes(EntityAttributeCreationEvent e) {
        e.put(TACEntities.WYVERN.get(), Wyvern.createWyvernAttributes().build());
        e.put(TACEntities.COCKATRICE.get(), Cockatrice.createCockatriceAttributes().build());
    }
}
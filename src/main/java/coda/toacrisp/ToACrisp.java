package coda.toacrisp;

import coda.toacrisp.common.entities.Cockatrice;
import coda.toacrisp.common.entities.Wyvern;
import coda.toacrisp.registry.TACBlocks;
import coda.toacrisp.registry.TACEntities;
import coda.toacrisp.registry.TACItems;
import coda.toacrisp.registry.TACParticles;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ToACrisp.MOD_ID)
public class ToACrisp {
    public static final String MOD_ID = "toacrisp";
    public static final CreativeModeTab GROUP = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.CARROT);
        }
    };

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
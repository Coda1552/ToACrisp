package codyy.toacrisp;

import codyy.toacrisp.common.entities.Cockatrice;
import codyy.toacrisp.common.entities.Wyvern;
import codyy.toacrisp.registry.TACBlocks;
import codyy.toacrisp.registry.TACEntities;
import codyy.toacrisp.registry.TACItems;
import codyy.toacrisp.registry.TACParticles;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        bus.addListener(this::createTab);
    }

    private void createAttributes(EntityAttributeCreationEvent e) {
        e.put(TACEntities.WYVERN.get(), Wyvern.createWyvernAttributes().build());
        e.put(TACEntities.COCKATRICE.get(), Cockatrice.createCockatriceAttributes().build());
    }

    private void createTab(CreativeModeTabEvent.Register e) {
        e.registerCreativeModeTab(new ResourceLocation(MOD_ID, MOD_ID), p -> p.icon(() -> new ItemStack(TACItems.WYVERN_SPAWN_EGG.get()))
                .title(Component.translatable("itemGroup." + MOD_ID))
                .displayItems((enabledFeatures, entries) -> {
                    for (var items : TACItems.ITEMS.getEntries()) {
                        entries.accept(items.get());
                    }
                })
        );
    }
}
package coda.toacrisp;

import coda.toacrisp.common.entities.CockatriceEntity;
import coda.toacrisp.registry.TACEntities;
import coda.toacrisp.registry.TACItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ToACrisp.MOD_ID)
public class ToACrisp {
    public static final String MOD_ID = "toacrisp";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreativeModeTab GROUP = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TACItems.COCKATRICE_SPAWN_EGG.get());
        }
    };

    public ToACrisp() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        TACEntities.ENTITIES.register(bus);
        TACItems.ITEMS.register(bus);

        bus.addListener(this::setEntityAttributes);
    }

    private void setEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(TACEntities.COCKATRICE.get(), CockatriceEntity.createAttributes().build());
    }

}

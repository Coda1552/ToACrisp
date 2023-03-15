package coda.toacrisp.registry;

import coda.toacrisp.ToACrisp;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TACItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ToACrisp.MOD_ID);

    public static final RegistryObject<Item> WYVERN_SPAWN_EGG = ITEMS.register("wyvern_spawn_egg", () -> new ForgeSpawnEggItem(TACEntities.WYVERN, 0x852738, 0xe6d34e, new Item.Properties().tab(ToACrisp.GROUP)));

    public static final RegistryObject<Item> ASH = ITEMS.register("ash", () -> new BlockItem(TACBlocks.ASH.get(), new Item.Properties().tab(ToACrisp.GROUP)));
}

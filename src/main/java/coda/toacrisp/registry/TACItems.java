package coda.toacrisp.registry;

import coda.toacrisp.ToACrisp;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TACItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ToACrisp.MOD_ID);

    public static final RegistryObject<Item> COCKATRICE_SPAWN_EGG = ITEMS.register("cockatrice_spawn_egg", () -> new ForgeSpawnEggItem(TACEntities.COCKATRICE, 0xfffee1, 0x769250, new Item.Properties().tab(ToACrisp.GROUP)));
}

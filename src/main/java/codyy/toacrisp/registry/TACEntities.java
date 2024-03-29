package codyy.toacrisp.registry;

import codyy.toacrisp.ToACrisp;
import codyy.toacrisp.common.entities.Cockatrice;
import codyy.toacrisp.common.entities.Wyvern;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TACEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ToACrisp.MOD_ID);

    public static final RegistryObject<EntityType<Wyvern>> WYVERN = ENTITIES.register("wyvern", () -> EntityType.Builder.<Wyvern>of(Wyvern::new, MobCategory.CREATURE).sized(1.5F, 1.5F).setTrackingRange(16).updateInterval(1).build("wyvern"));
    public static final RegistryObject<EntityType<Cockatrice>> COCKATRICE = ENTITIES.register("cockatrice", () -> EntityType.Builder.of(Cockatrice::new, MobCategory.CREATURE).sized(1.0f, 1.0f).setTrackingRange(16).updateInterval(1).build("cockatrice"));
}

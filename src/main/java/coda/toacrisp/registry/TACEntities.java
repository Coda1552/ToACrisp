package coda.toacrisp.registry;

import coda.toacrisp.ToACrisp;
import coda.toacrisp.common.entities.Wyvern;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TACEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ToACrisp.MOD_ID);

    public static final RegistryObject<EntityType<Wyvern>> WYVERN = ENTITIES.register("wyvern", () -> EntityType.Builder.of(Wyvern::new, MobCategory.CREATURE).sized(1.5F, 1.5F).setTrackingRange(16).updateInterval(1).build("wyvern"));
}

package coda.toacrisp.registry;

import coda.toacrisp.ToACrisp;
import coda.toacrisp.common.entities.CockatriceEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TACEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ToACrisp.MOD_ID);

    public static final RegistryObject<EntityType<CockatriceEntity>> COCKATRICE = create("cockatrice", EntityType.Builder.of(CockatriceEntity::new, MobCategory.CREATURE).sized(1.0f, 1.0f));

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(ToACrisp.MOD_ID + "." + name));
    }
}

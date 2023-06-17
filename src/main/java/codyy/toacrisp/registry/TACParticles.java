package codyy.toacrisp.registry;

import codyy.toacrisp.ToACrisp;
import codyy.toacrisp.client.particle.FlameParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TACParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ToACrisp.MOD_ID);

    public static final RegistryObject<SimpleParticleType> FLAME = PARTICLES.register("flame", () -> new SimpleParticleType(false));

    @Mod.EventBusSubscriber(modid = ToACrisp.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegisterParticleFactories {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void registerParticleTypes(RegisterParticleProvidersEvent e) {
            e.registerSpriteSet(FLAME.get(), FlameParticle.Provider::new);
        }
    }
}

package codyy.toacrisp.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlameParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public FlameParticle(ClientLevel p_107717_, double p_107718_, double p_107719_, double p_107720_, double p_107721_, double p_107722_, double p_107723_, SpriteSet set) {
        super(p_107717_, p_107718_, p_107719_, p_107720_, p_107721_, p_107722_, p_107723_);
        this.sprites = set;
        this.lifetime = 40;
        this.hasPhysics = true;
        this.setSpriteFromAge(set);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        super.tick();
        this.setParticleSpeed(0.0D, 0.0D, 0.0D);
        this.alpha = Math.min(age * 15, 255) / 255F;

        this.setSpriteFromAge(sprites);

        if (age >= 20) {
            this.scale(0.9F);
        }
        else {
            if (age % 2 == 0) {
                this.scale(1.1F);
            }
            this.rCol = 0.5F;
//            this.rCol = Mth.clamp(rCol, Math.max(age * 12.5F, 150) / 255F, 1.0F);
            this.gCol = Math.max(age * 6.5F, 100) / 255F;
            this.bCol = 0F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet p_107739_) {
            this.sprite = p_107739_;
        }

        public Particle createParticle(SimpleParticleType p_107750_, ClientLevel p_107751_, double p_107752_, double p_107753_, double p_107754_, double p_107755_, double p_107756_, double p_107757_) {
            FlameParticle particle = new FlameParticle(p_107751_, p_107752_, p_107753_, p_107754_, p_107755_, p_107756_, p_107757_, sprite);
            particle.setAlpha(1.0F);
            return particle;
        }
    }
}

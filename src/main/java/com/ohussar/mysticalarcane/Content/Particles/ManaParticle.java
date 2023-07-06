package com.ohussar.mysticalarcane.Content.Particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
public class ManaParticle extends SimpleAnimatedParticle {

    protected ManaParticle(ClientLevel level, double xcoord, double ycoord, double zcoord,
        SpriteSet SpriteSet, double xd, double yd, double zd) {
        super(level, xcoord, ycoord, zcoord, SpriteSet, 0.1f);
        this.friction = 0.8F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 0.75f;
        this.lifetime = 50;
        this.setSpriteFromAge(SpriteSet);
  
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
}

    @Override
    public void tick(){
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public ParticleRenderType getRenderType(){
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet spriteSet){
            this.sprites = spriteSet;
        }
        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x,
                double y, double z, double dx, double dy, double dz) {
            
            return new ManaParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
        
    }
}

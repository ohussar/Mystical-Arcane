package com.ohussar.mysticalarcane.Content.ArcaneWand.Projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
public class WandProjectile extends WandProjectileAbstract {
    public WandProjectile(EntityType<? extends WandProjectile> type, Level level) {
        super(type, level);
    }
    @Override
    public void tick(){
        super.tick();
        this.checkInsideBlocks();
        this.setPos(this.position().add(this.getDeltaMovement()));
        Vec3 direction = this.getDeltaMovement().normalize();
        for(int i = 0; i < 5; i++){
            this.level.addParticle(ParticleTypes.FIREWORK, this.getX() + direction.x*i, this.getY()+direction.y*i, this.getZ()+direction.z*i, 
            this.getDeltaMovement().x * (.1+i/5), this.getDeltaMovement().y * (.1+i/5), this.getDeltaMovement().z * (.1+i/5));
        }
        
    }
    @Override
    public void onInsideBlock(BlockState b){
        if(!b.isAir()){
            this.discard();
        }
    }


}

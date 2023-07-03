package com.ohussar.mysticalarcane.Content.ArcaneWand.Projectile;

import com.ohussar.mysticalarcane.Content.ItemAltar.ItemAltarBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
        
        //precise movement
        for(int i = 0; i < 3; i++){
            Vec3 newMove = this.getDeltaMovement().scale(.3);
            this.setPos(this.position().add(newMove));
            this.checkInsideBlocks();
        }

        Vec3 direction = this.getDeltaMovement().normalize();
        for(int i = 0; i < 5; i++){
            this.level.addParticle(ParticleTypes.FIREWORK, this.getX() + direction.x*i, this.getY()+direction.y*i, this.getZ()+direction.z*i, 
            0, 0, 0);
        }
        
    }
    @Override
    public void onInsideBlock(BlockState b){
        if(!b.isAir()){
            if(b.getBlock() instanceof ItemAltarBlock){
                Minecraft mine = Minecraft.getInstance();
                if(mine != null){
                    mine.player.sendSystemMessage(Component.literal("AAA"));
                }
            }
            this.discard();
        }
    }


}

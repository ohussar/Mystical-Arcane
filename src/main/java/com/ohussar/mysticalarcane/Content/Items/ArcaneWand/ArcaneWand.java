package com.ohussar.mysticalarcane.Content.Items.ArcaneWand;

import com.ohussar.mysticalarcane.API.UtilFunctions;
import com.ohussar.mysticalarcane.Base.AbstractManaBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class ArcaneWand extends Item {

    public ArcaneWand(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        
        if(hand == InteractionHand.MAIN_HAND){
            BlockPos pos = UtilFunctions.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE).getBlockPos();
            Block block = level.getBlockState(pos).getBlock();
            if(block instanceof AbstractManaBlock){
                if(!level.isClientSide()){
                    AbstractManaBlock interactible = (AbstractManaBlock) block;
                    interactible.actionOnClick(pos, level);
                    player.getItemInHand(hand).hurtAndBreak(1, player, null);
                    player.getCooldowns().addCooldown(this, 15);
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }else{
                    createParticles(level, player, pos);
                }
            }
        }
        return super.use(level, player, hand);
    }

    private void createParticles(Level level, Player player, BlockPos pos){  
        Vec3 startpos = player.getEyePosition();
        Vec3 endpos = new Vec3(pos.getX()+0.5, pos.getY()+0.8, pos.getZ()+0.5);
        Vec3 angle = endpos.subtract(startpos).normalize();
        double distance = startpos.distanceToSqr(endpos);
        int particleperunit = 2;
        int minparticles = 4;
        int particlecount = (int)(Math.sqrt(distance) * particleperunit);
        particlecount = Math.max(minparticles, particlecount);
        double factor = Math.sqrt(distance)/particlecount;
        for(int i = 1; i < particlecount; i++){
            level.addParticle(ParticleTypes.FIREWORK, 
            startpos.x + angle.x*i*factor, startpos.y + angle.y*i*factor, startpos.z + angle.z*i*factor, 
            0, 0, 0);
        }
    }
}

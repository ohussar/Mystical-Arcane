package com.ohussar.mysticalarcane.Content.ArcaneWand;

import com.ohussar.mysticalarcane.Base.ModEntities;
import com.ohussar.mysticalarcane.Content.ArcaneWand.Projectile.WandProjectile;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArcaneWand extends Item {

    public ArcaneWand(Properties properties) {
        super(properties);
        
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND){
            player.getCooldowns().addCooldown(this, 15);
            player.getItemInHand(hand).hurtAndBreak(1, player, null);
            WandProjectile proj = create_projectile(level, player);
            player.level.addFreshEntity(proj);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    public WandProjectile create_projectile(Level level, Player player){
        WandProjectile proj = new WandProjectile(ModEntities.WAND_PROJECTILE.get(), level);
        proj.setPos(player.getX(), player.getY()+1.5, player.getZ());
        Vec3 motion = player.getLookAngle().normalize().scale(3.0);
        proj.setDeltaMovement(motion);
        return proj;
    }

}

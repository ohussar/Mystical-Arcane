package com.ohussar.mysticalarcane.Content.ArcaneWand;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArcaneWand extends Item {

    public ArcaneWand(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND){
            player.getCooldowns().addCooldown(this, 15);
            player.getItemInHand(hand).hurtAndBreak(1, player, null);
            return InteractionResultHolder.consume(null);
        }
        return super.use(level, player, hand);
    }


}

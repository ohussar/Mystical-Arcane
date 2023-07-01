package com.ohussar.mysticalarcane.Content.ArcaneWand.Projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class WandProjectileAbstract extends Projectile{

    protected WandProjectileAbstract(EntityType<? extends WandProjectileAbstract> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
    }
    
}

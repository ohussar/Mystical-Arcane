package com.ohussar.mysticalarcane.Base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface AbstractManaInterface {
    void actionOnClick(BlockPos pos, Level level);
}

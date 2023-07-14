package com.ohussar.mysticalarcane.API;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class UtilFunctions {
    public static BlockHitResult getPlayerPOVHitResult(Level p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
      float f = p_41437_.getXRot();
      float f1 = p_41437_.getYRot();
      Vec3 vec3 = p_41437_.getEyePosition();
      float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
      float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
      float f6 = f3 * f4;
      float f7 = f2 * f4;
      double d0 = p_41437_.getReachDistance();
      Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
      return p_41436_.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, p_41438_, p_41437_));
   }

   public static Vec3 toVec3Offset(BlockPos pos, double xx, double yy, double zz){
      return new Vec3(pos.getX() + xx, pos.getY() + yy, pos.getZ() + zz);
   }

   public static Vec3 toVec3(BlockPos pos){
      return new Vec3(pos.getX(), pos.getY(), pos.getZ());
   }
}

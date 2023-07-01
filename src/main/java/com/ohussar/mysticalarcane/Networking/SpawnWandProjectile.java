package com.ohussar.mysticalarcane.Networking;

import java.util.function.Supplier;

import com.ohussar.mysticalarcane.Base.ModEntities;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

public class SpawnWandProjectile {
    public SpawnWandProjectile(){
    }
    public SpawnWandProjectile(FriendlyByteBuf buf){
    }

    public void toBytes(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
           ServerPlayer player = context.getSender();
           ServerLevel level = player.getLevel();
           ModEntities.WAND_PROJECTILE.get().spawn(level, null, null, player, player.blockPosition(), 
           MobSpawnType.EVENT, true, false);
        });
        return true;
    }

}

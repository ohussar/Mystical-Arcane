package com.ohussar.mysticalarcane.Networking.Tank;

import java.util.function.Supplier;

import com.ohussar.mysticalarcane.Content.Tank.TankEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncFuelCount {
    private final int value;
    private final BlockPos pos;
    public SyncFuelCount(int value, BlockPos pos){
        this.value = value;
        this.pos = pos;
    }
    public SyncFuelCount(FriendlyByteBuf buf){
       this.value = buf.readInt();
       this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(value);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mine = Minecraft.getInstance();
            if(mine.level.getBlockEntity(pos) instanceof TankEntity blockEntity){
                blockEntity.setFuelCount(value);
            }
        });
        return true;
    }
}

package com.ohussar.mysticalarcane.Networking;

import java.util.function.Supplier;

import com.ohussar.mysticalarcane.API.IButtonCall;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ButtonCall {
    private final BlockPos pos;
    public ButtonCall(BlockPos pos){
        this.pos = pos;
    }
    public ButtonCall(FriendlyByteBuf buf){
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Level level = context.getSender().level;
            if(level.getBlockEntity(pos) instanceof IButtonCall callable){
                callable.onButtonCall();
            }
        });
        return true;
    }
}
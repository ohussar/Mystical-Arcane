package com.ohussar.mysticalarcane.Networking;

import java.util.function.Supplier;

import com.ohussar.mysticalarcane.Content.ItemAltar.ItemAltarBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncHeightModelAltar {
    private final float height;
    private final BlockPos pos;
    public SyncHeightModelAltar(float height, BlockPos pos){
        this.height = height;
        this.pos = pos;
    }
    public SyncHeightModelAltar(FriendlyByteBuf buf){
        this.height = buf.readFloat();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(height);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mine = Minecraft.getInstance();
            if(mine.level.getBlockEntity(pos) instanceof ItemAltarBlockEntity blockEntity){
                blockEntity.craftingModelHeight = height;
            }
        });
        return true;
    }

}

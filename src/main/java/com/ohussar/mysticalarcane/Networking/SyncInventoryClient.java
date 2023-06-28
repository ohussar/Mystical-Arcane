package com.ohussar.mysticalarcane.Networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.function.Supplier;

import com.ohussar.mysticalarcane.Content.ItemAltar.ItemAltarBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

public class SyncInventoryClient {
    private final ItemStackHandler handler;
    private final BlockPos pos;
    public SyncInventoryClient(ItemStackHandler handler, BlockPos pos){
        this.handler = handler;
        this.pos = pos;
    }
    public SyncInventoryClient(FriendlyByteBuf buf){
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        handler = new ItemStackHandler(collection.size());
        for(int i = 0; i < collection.size(); i++){
            handler.insertItem(i, collection.get(i), false);
        }
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf){
        Collection<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < handler.getSlots(); i++){
            list.add(handler.getStackInSlot(i));
        }
        buf.writeCollection(list, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mine = Minecraft.getInstance();
            if(mine.level.getBlockEntity(pos) instanceof ItemAltarBlockEntity blockEntity){
                blockEntity.setHandler(this.handler);
            }
        });
        return true;
    }

}

package com.ohussar.mysticalarcane.Networking;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import com.ohussar.mysticalarcane.API.IVariablesUpdate;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncVariables {
    private final Object[] obj;
    private final BlockPos pos;
    public SyncVariables(Object[] obj, BlockPos pos){
        this.obj = obj;
        this.pos = pos;
    }
    public SyncVariables(FriendlyByteBuf buf){
       String a = new String(buf.readByteArray(), StandardCharsets.UTF_8);
       int b = a.length();
       this.obj = new Object[b];
       for(int i = 0; i < b; i++){
            switch(a.charAt(i)){
                case 'i':
                    this.obj[i] = buf.readInt();
                break;
                case 's':
                    this.obj[i] = new String(buf.readByteArray(), StandardCharsets.UTF_8);
                break;
                case 'd':
                    this.obj[i] = buf.readDouble();
                break;
                case 'f':
                    this.obj[i] = buf.readFloat();
                break;

            }
       }
       this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf){
        String a = "";
        for(int i = 0; i < obj.length; i++){
            if(obj[i] instanceof Integer){
                a+="i";
            }else if(obj[i] instanceof String){
                a+="s";
            }else if(obj[i] instanceof Double){
                a+="d";
            }else if(obj[i] instanceof Float){
                a+="f";
            }
        }
        buf.writeByteArray(a.getBytes(StandardCharsets.UTF_8));
        for(int i = 0; i < obj.length; i++){
            if(obj[i] instanceof Integer){
                buf.writeInt((int) obj[i]);
            }else if(obj[i] instanceof String){
                buf.writeBytes(((String)obj[i]).getBytes(StandardCharsets.UTF_8));
            }else if(obj[i] instanceof Double){
                buf.writeDouble((double) obj[i]);
            }else if(obj[i] instanceof Float){
                buf.writeFloat((float) obj[i]);
            }
        }
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mine = Minecraft.getInstance();
            if(mine.level.getBlockEntity(pos) instanceof IVariablesUpdate blockEntity){
                blockEntity.onVariablesUpdated(obj);
            }
        });
        return true;
    }

}

package com.ohussar.mysticalarcane.Content.Holder;

import java.util.Optional;

import com.ohussar.mysticalarcane.API.IContentsChangedUpdate;
import com.ohussar.mysticalarcane.API.UtilFunctions;
import com.ohussar.mysticalarcane.Base.BlockEntityContainer;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Base.ModParticles;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.Recipes.HolderRecipe;
import com.ohussar.mysticalarcane.Content.Tank.TankEntity;
import com.ohussar.mysticalarcane.Networking.ModMessages;
import com.ohussar.mysticalarcane.Networking.SpawnParticles;
import com.ohussar.mysticalarcane.Networking.SyncInventoryClient;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class HolderEntity extends BlockEntityContainer implements IContentsChangedUpdate {
    private int checkTimer = 0;
    protected boolean changed = false;
    public HolderEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOLDER_ENTITY.get(), pos, state);
        holder = new ItemStackHandler(1){
            @Override
            public void onContentsChanged(int slot){
                setChanged();
                if(!level.isClientSide()){
                    ModMessages.sendToClients(new SyncInventoryClient(this, worldPosition));
                }
            }
        };
    }
    public void setHandler(ItemStackHandler itemStackHandler) {
        for(int i = 0; i < itemStackHandler.getSlots(); i++){
            holder.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    public void tryCraft(Level level, HolderEntity entity){
        if(hasRecipe(entity)){
            BlockEntity below = level.getBlockEntity(entity.worldPosition.below());
            if(below instanceof TankEntity blockEntity){
                int con = blockEntity.getFuelCount() - getRecipe(entity).get().getFuelConsume();
                if(con >= 0){
                    blockEntity.setFuelCount(con);
                    blockEntity.SyncVariables();
                    holder.setStackInSlot(0, getRecipe(entity).get().getResultItem());
                    ModMessages.sendToClients(new SpawnParticles(
                        ModParticles.MANA_PARTICLE.get(), 
                        UtilFunctions.toVec3Offset(entity.worldPosition, 0.5, 0, 0.5), 
                        20, 1.25, 0.5f));
                }
            }
        }
    }

    public void onClick(Level level, Player player){
        ItemStack holding = holder.getStackInSlot(0);
        if(holding == ItemStack.EMPTY){
            ItemStack hand = player.getMainHandItem().copy();
            hand.setCount(1);
            holder.setStackInSlot(0, hand);
            ItemStack n = player.getMainHandItem();
            n.setCount(n.getCount() - 1);
            if(n.getCount() == 0){
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }else{
                player.setItemInHand(InteractionHand.MAIN_HAND, n);
            }

        }else if(holding.getItem() != player.getMainHandItem().getItem()
                && holding.getCount() == 1 && (player.getMainHandItem().getCount() == 1 || 
                player.getMainHandItem() == ItemStack.EMPTY)){
            ItemStack temp = holding.copy();
            holder.setStackInSlot(0, player.getMainHandItem());
            if(temp == ItemStack.EMPTY){
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }else{
                player.setItemInHand(InteractionHand.MAIN_HAND, temp);
            }
        }else if(holding.getItem() == player.getMainHandItem().getItem()){
            ItemStack temp = player.getMainHandItem();
            int c = holding.getCount() + temp.getCount();
            if(c > holding.getItem().getMaxStackSize(holding)){
                temp.setCount(holding.getItem().getMaxStackSize(holding));
                player.setItemInHand(InteractionHand.MAIN_HAND, temp);
                int surpass = c - holding.getItem().getMaxStackSize(holding);
                holding.setCount(surpass);
                holder.setStackInSlot(0, holding);
            }else{
                temp.setCount(c);
                player.setItemInHand(InteractionHand.MAIN_HAND, temp);
                holder.setStackInSlot(0, ItemStack.EMPTY);
            }
        }else if(player.getMainHandItem() == ItemStack.EMPTY){
            player.setItemInHand(InteractionHand.MAIN_HAND, holding);
            holder.setStackInSlot(0, ItemStack.EMPTY);
        }
    }
    
    @Override
    public void onContentsChanged(ItemStackHandler handler) {
        setHandler(handler);
    }
    public static void tick(Level level, BlockPos pos, BlockState state, HolderEntity entity){
        if(level.isClientSide()) return;
        if(entity.changed){
            entity.changed = false;
            entity.checkTimer = 10;
        }
        entity.checkTimer++;
        if(entity.checkTimer >= 5){
            entity.checkTimer = 0;
            BlockState below = level.getBlockState(pos.below());
            if(below.getBlock() == ModBlocks.TANK.get()){
                level.setBlock(pos, state.setValue(Holder.isAbove, true), 3);
            }else{
                level.setBlock(pos, state.setValue(Holder.isAbove, false), 3);
            }
        }
    }
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(){
        return saveWithFullMetadata();
    }

    private static Optional<HolderRecipe> getRecipe(HolderEntity entity){
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, entity.holder.getStackInSlot(0));

        Optional<HolderRecipe> recipe = level.getRecipeManager().getRecipeFor
        (HolderRecipe.Type.INSTANCE, inventory, level); 
        return recipe;
    }
    public static boolean hasRecipe(HolderEntity entity){
        return getRecipe(entity).isPresent();
    }
}

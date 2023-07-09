package com.ohussar.mysticalarcane.Content.ItemAltar;

import java.util.Optional;
import com.ohussar.mysticalarcane.API.IContentsChangedUpdate;
import com.ohussar.mysticalarcane.Base.BlockEntityContainer;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Base.ModParticles;
import com.ohussar.mysticalarcane.Base.Multiblock;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.Recipes.ItemAltarRecipe;
import com.ohussar.mysticalarcane.Networking.ModMessages;
import com.ohussar.mysticalarcane.Networking.SpawnParticles;
import com.ohussar.mysticalarcane.Networking.SyncHeightModelAltar;
import com.ohussar.mysticalarcane.Networking.SyncInventoryClient;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
public class ItemAltarBlockEntity extends BlockEntityContainer implements IContentsChangedUpdate {
    static String[] struct = {"k", "k", "x", "k", "k",
                              "k", "o", "o", "o", "k",
                              "x", "o", "a", "o", "x",
                              "k", "o", "o", "o", "k",
                              "k", "k", "x", "k", "k"};
    private static int offsetX = 2;
    private static int offsetZ = 2;
    private static int limitPerFlower = 2;
    private static int flowerCount = 4;
    private static Block flowerBlock = ModBlocks.MANA_ORCHID.get();
    private static Multiblock structure = new Multiblock(struct, 5, 5);
    private static int multiblockCheckTimer = 0;
    private static boolean isAssembled = false;
    public boolean isCrafting = false;
    private int craftingStage = 0;
    public float craftingModelHeightMin = 0.7f;
    public float craftingModelHeight = 0.7f;
    public float craftingModelHeightMax = 2.0f;
    private static int timer = 0;

    public ItemAltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ITEM_ALTAR_ENTITY.get(), blockPos, blockState);
        structure.setDictionaryKey("x", flowerBlock);
        structure.setDictionaryKey("o", Blocks.AIR);
        structure.setDictionaryKey("a", ModBlocks.ITEM_ALTAR.get());
        structure.setDictionaryKey("k", null);
        this.holder = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot){
            setChanged();
            if(!level.isClientSide()){
                ModMessages.sendToClients(new SyncInventoryClient(this, worldPosition));
                }
            }
        };
    }
    public void exchangeItem(ItemStack stack, Player player){
        player.setItemInHand(InteractionHand.MAIN_HAND, holder.getStackInSlot(0));
        holder.setStackInSlot(0, stack);
    }

    public ItemStack getHolding(){
        return holder.getStackInSlot(0);
    }


    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(){
        return saveWithFullMetadata();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ItemAltarBlockEntity entity){
        multiblockCheckTimer++;
        if(multiblockCheckTimer >= 10){
            multiblockCheckTimer = 0;
            BlockPos target = new BlockPos(pos.getX()-offsetX, pos.getY(), pos.getZ()-offsetZ);
            isAssembled = structure.checkIfAssembled(level, target);
            target = null;
        }

        if(level.isClientSide()){
            return;
        }
        if(isAssembled && entity.isCrafting){
            if(entity.craftingStage == 0){
                entity.craftingModelHeight += 0.025f;
                for(int k = 0; k < flowerCount; k++){
                    BlockPos f = new BlockPos(pos.getX() + sin(k)*2, pos.getY(), pos.getZ() + cos(k)*2);
                    BlockState b = level.getBlockState(f);
                    if(b.getBlock() == flowerBlock){
                        ModMessages.sendToClients(
                            new SpawnParticles(ModParticles.MANA_PARTICLE.get(),
                            new Vec3(f.getX() + 0.5, f.getY(), f.getZ() + 0.5), 
                            new Vec3(pos.getX() + 0.5, pos.getY() + entity.craftingModelHeight, pos.getZ() + 0.5), 1, 0.075)
                            );
                    }
                }
                if(entity.craftingModelHeight >= entity.craftingModelHeightMax){
                    entity.craftingModelHeight = entity.craftingModelHeightMax;
                    entity.craftingStage = 1;
                }
                ModMessages.sendToClients(new SyncHeightModelAltar(entity.craftingModelHeight, pos));
            }
            if(entity.craftingStage == 1){
                timer++;
                if(timer >= 25){
                    timer = 0;
                    entity.craftingStage = 2;
                }
            }
            if(entity.craftingStage == 2){

                    if(hasRecipe(entity)){
                        int itemCount = entity.holder.getStackInSlot(0).getCount();
                        ModMessages.sendToClients(new SpawnParticles(ModParticles.MANA_PARTICLE.get(), 
                        new Vec3(pos.getX()+0.5, pos.getY() + 2, pos.getZ() + 0.5), itemCount * 4, 1, 1.25));

                        entity.holder.setStackInSlot(0, 
                        new ItemStack(getRecipe(entity).get().getResultItem().getItem(), itemCount));

                        int angles = (int)Math.ceil((double)itemCount/(double)limitPerFlower);
                        for(int k = 0; k < angles; k++){
                            BlockPos f = new BlockPos(pos.getX() + sin(k)*2, pos.getY(), pos.getZ() + cos(k)*2);
                            BlockState b = level.getBlockState(f);
                            if(b.getBlock() == flowerBlock){
                                level.setBlock(f, Blocks.AIR.defaultBlockState(), 1);
                                level.sendBlockUpdated(f, b, Blocks.AIR.defaultBlockState(), 2);
                                ModMessages.sendToClients(new SpawnParticles(ModParticles.MANA_PARTICLE.get(), 
                                new Vec3(f.getX() + 0.5, f.getY() + 0.25, f.getZ() + 0.5), 8, 0.5, 0.5));
                            }
                        }
                    }else{
                        entity.craftingStage = 3;
                    }
            }
        }
        if(entity.craftingStage == 2 && entity.isCrafting){
            timer++;
            if(timer >= 10){
                timer = 0;
                entity.craftingStage = 3;
            }
        }
        if(entity.craftingStage == 3 && entity.isCrafting){
            entity.craftingModelHeight -= 0.025f;
            if(entity.craftingModelHeight <= entity.craftingModelHeightMin){
                entity.craftingModelHeight = entity.craftingModelHeightMin;
                entity.craftingStage = 0;
                entity.isCrafting = false;
                timer = 0;
            }
            ModMessages.sendToClients(new SyncHeightModelAltar(entity.craftingModelHeight, pos));
        }
    }

    private static int cos(int index){
        switch(index){
            case 0:
                return 1;
            case 1:
                return 0;
            case 2:
                return -1;
            case 3:
                return 0;
            default:
                return 0;
        }
    }
   
    private static int sin(int index){
        switch(index){
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 0;
            case 3:
                return -1;
            default:
                return 0;
        }
    } 

    private static Optional<ItemAltarRecipe> getRecipe(ItemAltarBlockEntity entity){
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, entity.holder.getStackInSlot(0));

        Optional<ItemAltarRecipe> recipe = level.getRecipeManager().getRecipeFor
        (ItemAltarRecipe.Type.INSTANCE, inventory, level); 
        return recipe;
    }
    public static boolean hasRecipe(ItemAltarBlockEntity entity){
        return getRecipe(entity).isPresent();
    }

    public void startCrafting(ItemAltarBlockEntity entity){
        if(isAssembled && hasRecipe(entity) && entity.holder.getStackInSlot(0).getCount() <= (limitPerFlower * flowerCount)){
            isCrafting = true;
        }
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for(int i = 0; i < itemStackHandler.getSlots(); i++){
            holder.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(holder.getSlots());
        for(int i = 0; i < holder.getSlots(); i++){
            inventory.setItem(i, holder.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    @Override
    public void onContentsChanged(ItemStackHandler handler) {
        setHandler(handler);
    }
}

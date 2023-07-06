package com.ohussar.mysticalarcane.Content.ItemAltar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Base.ModParticles;
import com.ohussar.mysticalarcane.Base.Multiblock;
import com.ohussar.mysticalarcane.Content.Items;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Networking.ModMessages;
import com.ohussar.mysticalarcane.Networking.SpawnParticles;
import com.ohussar.mysticalarcane.Networking.SyncHeightModelAltar;
import com.ohussar.mysticalarcane.Networking.SyncInventoryClient;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
public class ItemAltarBlockEntity extends BlockEntity {
    public ItemStackHandler holding = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot){
            setChanged();
            if(!level.isClientSide()){
                ModMessages.sendToClients(new SyncInventoryClient(this, worldPosition));
            }
        }
    };

    static String[] struct = {"k", "k", "x", "k", "k",
                              "k", "o", "o", "o", "k",
                              "x", "o", "a", "o", "x",
                              "k", "o", "o", "o", "k",
                              "k", "k", "x", "k", "k"};
    private static int offsetX = 2;
    private static int offsetZ = 2;

    private static Multiblock structure = new Multiblock(struct, 5, 5);
    private static int multiblockCheckTimer = 0;
    private static boolean isAssembled = false;
    public boolean isCrafting = false;
    private int craftingStage = 0;
    public float craftingModelHeightMin = 0.7f;
    public float craftingModelHeight = 0.7f;
    public float craftingModelHeightMax = 2.0f;
    private static int timer = 0;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ItemAltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ITEM_ALTAR_ENTITY.get(), blockPos, blockState);
        structure.setDictionaryKey("x", ModBlocks.MANA_ORCHID.get());
        structure.setDictionaryKey("o", Blocks.AIR);
        structure.setDictionaryKey("a", ModBlocks.ITEM_ALTAR.get());
        structure.setDictionaryKey("k", null);
    }
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    } 

    public void exchangeItem(ItemStack stack, Player player){
        player.setItemInHand(InteractionHand.MAIN_HAND, holding.getStackInSlot(0));
        holding.setStackInSlot(0, stack);
    }

    public ItemStack getHolding(){
        return holding.getStackInSlot(0);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> holding);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(){
        return saveWithFullMetadata();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", holding.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        holding.deserializeNBT(nbt.getCompound("inventory"));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ItemAltarBlockEntity entity){
        multiblockCheckTimer++;
        if(multiblockCheckTimer >= 10){
            multiblockCheckTimer = 0;
            BlockPos target = new BlockPos(pos.getX()-offsetX, pos.getY(), pos.getZ()-offsetZ);
            isAssembled = structure.checkIfAssembled(level, target);
        }

        if(level.isClientSide()){
            return;
        }
        if(isAssembled && entity.isCrafting){
            if(entity.craftingStage == 0){
                entity.craftingModelHeight += 0.01f;
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
                if(timer == 0){
                    ModMessages.sendToClients(new SpawnParticles(ModParticles.MANA_PARTICLE.get(), 
                    new Vec3(pos.getX()+0.5, pos.getY() + 2, pos.getZ() + 0.5), 25, 1));
                    
                    entity.holding.setStackInSlot(0, new ItemStack(Items.MANA_INGOT.get()));
                }
                timer++;
                if(timer >= 10){
                    timer = 0;
                    entity.craftingStage = 3;
                }
            }
            if(entity.craftingStage == 3){
                entity.craftingModelHeight -= 0.01f;
                if(entity.craftingModelHeight <= entity.craftingModelHeightMin){
                    entity.craftingModelHeight = entity.craftingModelHeightMin;
                    entity.craftingStage = 0;
                    entity.isCrafting = false;
                }
                ModMessages.sendToClients(new SyncHeightModelAltar(entity.craftingModelHeight, pos));
                
            }
        }
        
    }


    public void startCrafting(){
        if(isAssembled){
            isCrafting = true;
        }
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for(int i = 0; i < itemStackHandler.getSlots(); i++){
            holding.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(holding.getSlots());
        for(int i = 0; i < holding.getSlots(); i++){
            inventory.setItem(i, holding.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}

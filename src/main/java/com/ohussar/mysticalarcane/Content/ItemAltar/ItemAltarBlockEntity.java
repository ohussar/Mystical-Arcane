package com.ohussar.mysticalarcane.Content.ItemAltar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Networking.ModMessages;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ItemAltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ITEM_ALTAR_ENTITY.get(), blockPos, blockState);
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
        if(level.isClientSide()){
            return;
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

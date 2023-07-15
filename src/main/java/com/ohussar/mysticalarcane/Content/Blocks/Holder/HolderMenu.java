package com.ohussar.mysticalarcane.Content.Blocks.Holder;

import com.ohussar.mysticalarcane.Base.ModMenus;
import com.ohussar.mysticalarcane.Base.Menus.InputSlotHolder;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class HolderMenu extends AbstractContainerMenu {

    private final HolderEntity blockEntity;
    private final Level level;
    private final Player player;

    public HolderMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
       this(id, inventory, inventory.player.level.getBlockEntity(extraData.readBlockPos()), inventory.player);
    }
    public HolderMenu(int id, Inventory inv, BlockEntity entity, Player player){
        super(ModMenus.HOLDER_MENU.get(), id);
        this.level = player.level;
        this.player = player;
        this.blockEntity = (HolderEntity) entity;
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            int x1 = 53;
            int y1 = 28;
            int x2 = 107;
            int y2 = 28;
            this.addSlot(new InputSlotHolder(handler, 0, x1, y1));
            this.addSlot(new InputSlotHolder(handler, 1, x2, y2));
        });   
    }
    
    //---------------------------------------------------------------------------------------------------//
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    
    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
    //-----------------------------------------------------------------------//
    public HolderEntity getBlockEntity(){
        return this.blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
            this.player, 
            ModBlocks.HOLDER.get());
    }
    private void addPlayerInventory(Inventory playerInventory){
        for(int i = 0; i < 3; ++i){
            for(int l = 0; l < 9; ++l){
                this.addSlot(new Slot(playerInventory, l + i*9 + 9, 8 + l * 18, 84 + i*18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory){
        for(int i = 0; i < 9; ++i){
            this.addSlot(new Slot(playerInventory, i, 8+i*18, 142));
        }
    }
}

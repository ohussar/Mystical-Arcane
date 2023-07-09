package com.ohussar.mysticalarcane.Content.Tank;
import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.API.IContentsChangedUpdate;
import com.ohussar.mysticalarcane.API.IVariablesUpdate;
import com.ohussar.mysticalarcane.Base.BlockEntityContainer;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Base.Multiblock;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.ManaReceptor.ManaReceptorBlock;
import com.ohussar.mysticalarcane.Networking.ModMessages;
import com.ohussar.mysticalarcane.Networking.SyncInventoryClient;
import com.ohussar.mysticalarcane.Networking.SyncVariables;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class TankEntity extends BlockEntityContainer implements IContentsChangedUpdate, IVariablesUpdate {

    private String[] map = {"o", "o", "x", "o", "o",
                            "o", "o", "p", "o", "o",
                            "x", "p", "t", "p", "x",
                            "o", "o", "p", "o", "o",
                            "o", "o", "x", "o", "o" };
    Multiblock multi = new Multiblock(map, 5, 5);

    public TankEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TANK_ENTITY.get(), pos, state);
        this.holder = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot){
                setChanged();
                if(!level.isClientSide()){
                    ModMessages.sendToClients(new SyncInventoryClient(this, worldPosition));
                }
            }
        };
        multi.setDictionaryKey("o", null);
        multi.setDictionaryKey("x", ModBlocks.MANA_RECEPTOR.get());
        multi.setDictionaryKey("t", ModBlocks.TANK.get());
        multi.setDictionaryKey("p", Blocks.AIR);
    }

    protected Item fuel = Items.REDSTONE;
    protected int fuelCount = 0;
    public int maxFuelCount = 8;

    public boolean onItemClick(ItemStack stack, Player player){
        if(stack.getItem() == fuel){
            if(fuelCount < maxFuelCount){
                fuelCount++;
                stack.setCount(stack.getCount()-1);
                SyncVariables();
                return true;
            }
        }
        return false;
    }

    public boolean checkAssembled(Level level){
        if(multi.checkIfAssembled(level, worldPosition.offset(-2, 0, -2))){
            BlockPos[] list = multi.getListOfBlocksPlaced("x", level, worldPosition.offset(-2, 0, -2));
            boolean valid = true;
            Main.LOGGER.info(Integer.toString(list.length));
            for(int k = 0; k < list.length; k++){
                BlockState state = level.getBlockState(list[k]);
                if(state.getBlock() instanceof ManaReceptorBlock block){
                    BlockPos newpos = list[k];
                    newpos = newpos.relative(state.getValue(DirectionalBlock.FACING), 2);
                    Main.LOGGER.info(newpos.toString());
                    if(!(level.getBlockState(newpos).getBlock() instanceof Tank)){
                        valid = false;
                    }
                }
            }
            return valid;
        }
        return false;
    }

    @Override
    public void onVariablesUpdated(Object[] obj) {
        setFuelCount((int)obj[0]);
    }
    public void setFuelCount(int value){
        this.fuelCount = value;
    }

    public int getFuelCount(){
        return this.fuelCount;
    }
    public void SyncVariables(){
        Object[] param = new Object[1]; param[0] = fuelCount;
        ModMessages.sendToClients(new SyncVariables(param, this.worldPosition));
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("fuelCount", fuelCount);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        fuelCount = nbt.getInt("fuelCount");
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(){
        return saveWithFullMetadata();
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for(int i = 0; i < itemStackHandler.getSlots(); i++){
            holder.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }
    @Override
    public void onContentsChanged(ItemStackHandler handler) {
        setHandler(handler);
    }


}

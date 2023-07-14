package com.ohussar.mysticalarcane.Content.Blocks.Tank;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import com.ohussar.mysticalarcane.API.IContentsChangedUpdate;
import com.ohussar.mysticalarcane.API.IVariablesUpdate;
import com.ohussar.mysticalarcane.API.UtilFunctions;
import com.ohussar.mysticalarcane.Base.BlockEntityContainer;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Base.ModFluids;
import com.ohussar.mysticalarcane.Base.ModParticles;
import com.ohussar.mysticalarcane.Base.Multiblock;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.ModItems;
import com.ohussar.mysticalarcane.Content.Blocks.ManaReceptor.ManaReceptorBlock;
import com.ohussar.mysticalarcane.Networking.ModMessages;
import com.ohussar.mysticalarcane.Networking.SpawnParticles;
import com.ohussar.mysticalarcane.Networking.SyncInventoryClient;
import com.ohussar.mysticalarcane.Networking.SyncVariables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TankEntity extends BlockEntityContainer implements IContentsChangedUpdate, IVariablesUpdate, IFluidHandler {
    
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
    private int manaTimer = 0;
    protected Item fuel = Items.REDSTONE;
    protected int fuelCount = 0;
    public int maxFuelCount = 8;
    private int fluidCapacity = 2000; // mb
    private int fluidCount = 0;
    public boolean onItemClick(ItemStack stack, Player player){
        if(stack.getItem() == ModItems.MANA_WATER_BUCKET.get()){
            if(fluidCapacity-fluidCount >= 1000){
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                fluidCount += 1000;
                SyncVariables();
                return true;
            }
        }else if(stack.getItem() == Items.BUCKET){
            if(fluidCount >= 1000){
                fluidCount -= 1000;
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.MANA_WATER_BUCKET.get()));
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
            for(int k = 0; k < list.length; k++){
                BlockState state = level.getBlockState(list[k]);
                if(state.getBlock() instanceof ManaReceptorBlock block){
                    BlockPos newpos = list[k];
                    newpos = newpos.relative(state.getValue(DirectionalBlock.FACING), 2);
                    if(!(level.getBlockState(newpos).getBlock() instanceof Tank)){
                        valid = false;
                    }
                }
            }
            return valid;
        }
        return false;
    }
    public static void tick(Level level, BlockPos pos, BlockState state, TankEntity entity){
        if(level.isClientSide()){
            return;
        }
        entity.manaTimer--;
        if(entity.manaTimer <= 0){
            Random rand = new Random();
            entity.manaTimer = Math.max(5, rand.nextInt(45));
            
            if(entity.checkAssembled(level)){
                entity.fluidCount += 1+rand.nextInt(15);
                entity.SyncVariables();
                BlockPos[] list = entity.multi.getListOfBlocksPlaced("x", level, pos.offset(-2, 0, -2));
                for(int k = 0; k < list.length; k++){
                    BlockState s = level.getBlockState(list[k]);
                    if(s.getBlock() instanceof ManaReceptorBlock){
                        Direction dir = s.getValue(DirectionalBlock.FACING);
                        
                        ModMessages.sendToClients(new SpawnParticles(ModParticles.MANA_PARTICLE.get(), 
                        UtilFunctions.toVec3Offset(list[k], -dir.getStepZ()*0.5, 0.3, dir.getStepX()*0.5), 
                        UtilFunctions.toVec3Offset(pos    , -dir.getStepZ()*0.5, 0.3, dir.getStepX()*0.5), 
                        4, 0.15));
                    }
                }

            }

        }

    }
    @Override
    public void onVariablesUpdated(Object[] obj) {
        setFluidCount((int)obj[0]);
    }
    public void setFluidCount(int value){
        this.fluidCount = value;
    }

    public int getFluidCount(){
        return this.fluidCount;
    }
    public void SyncVariables(){
        Object[] param = new Object[1]; param[0] = fluidCount;
        ModMessages.sendToClients(new SyncVariables(param, this.worldPosition));
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("fluidCount", fluidCount);
        nbt.putInt("manaTimer", manaTimer);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        fluidCount = nbt.getInt("fluidCount");
        manaTimer = nbt.getInt("manaTimer");
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

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if(fluidCount > 0){
            return new FluidStack(ModFluids.SOURCE_MANA_WATER.get(), fluidCount);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.fluidCapacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        if(stack.getRawFluid() == ModFluids.SOURCE_MANA_WATER.get()){
            return true;
        }
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {

        int amount = resource.getAmount();
        int filled = 0;
        if(action == FluidAction.EXECUTE){
            int toFill = fluidCapacity - fluidCount;
            filled = toFill;
            if(amount <= toFill){
                fluidCount += amount;
                filled = amount;
            }else{
                fluidCount = fluidCapacity;
            }
        }else{
            int toFill = fluidCapacity - fluidCount;
            filled = toFill;
        }

        return filled;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if(resource.getRawFluid() == ModFluids.SOURCE_MANA_WATER.get()){
            if(action == FluidAction.EXECUTE){
                if(resource.getAmount() <= fluidCount){
                    FluidStack fluid = new FluidStack(ModFluids.SOURCE_MANA_WATER.get(), resource.getAmount());
                    fluidCount = fluidCount - resource.getAmount();
                    return fluid;
                }else{
                    FluidStack fluid = new FluidStack(ModFluids.SOURCE_MANA_WATER.get(), fluidCount);
                    fluidCount = 0;
                    return fluid;
                }
            }else{
                return new FluidStack(ModFluids.SOURCE_MANA_WATER.get(), resource.getAmount());
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return FluidStack.EMPTY;
    }
}

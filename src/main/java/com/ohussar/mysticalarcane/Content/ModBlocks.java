package com.ohussar.mysticalarcane.Content;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.ItemAltar.ItemAltarBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
    DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    public static final RegistryObject<Block> ITEM_ALTAR = BLOCKS.register("itemaltar", 
    () -> new ItemAltarBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.IRON_BLOCK)));
    public static void registerBlocks(IEventBus bus){
        BLOCKS.register(bus);
    }
}

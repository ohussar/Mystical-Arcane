package com.ohussar.mysticalarcane.Content;

import com.ohussar.mysticalarcane.Main;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = 
    DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

    public static void registerBlocks(IEventBus bus){
        BLOCKS.register(bus);
    }
}

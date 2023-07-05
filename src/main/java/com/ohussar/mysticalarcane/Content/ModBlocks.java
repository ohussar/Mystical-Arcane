package com.ohussar.mysticalarcane.Content;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.ItemAltar.ItemAltarBlock;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
    DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    public static final RegistryObject<Block> ITEM_ALTAR = BLOCKS.register("itemaltar", 
    () -> new ItemAltarBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(3, 5)));
    public static final RegistryObject<Block> MANA_ORCHID = BLOCKS.register("manaorchid", 
    () -> new FlowerBlock(() -> MobEffects.GLOWING, 1, BlockBehaviour.Properties.copy(Blocks.BLUE_ORCHID).offsetType(OffsetType.XZ)));
    public static final RegistryObject<Block> POTTED_MANA_ORCHID = BLOCKS.register("pottedmanaorchid", 
    () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.MANA_ORCHID, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static void registerBlocks(IEventBus bus){
        BLOCKS.register(bus);
    }
}

package com.ohussar.mysticalarcane.Base;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.Blocks;
import com.ohussar.mysticalarcane.Content.ItemAltar.ItemAltarBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);

    public static final RegistryObject<BlockEntityType<ItemAltarBlockEntity>> ITEM_ALTAR_ENTITY = 
        BLOCK_ENTITIES.register("itemaltar", 
        () -> BlockEntityType.Builder.of(ItemAltarBlockEntity::new, Blocks.ITEM_ALTAR.get()).build(null));

    public static void registerBlockEntitiesTypes(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}

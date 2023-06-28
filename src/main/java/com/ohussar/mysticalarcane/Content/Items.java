package com.ohussar.mysticalarcane.Content;

import com.ohussar.mysticalarcane.Main;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, 
    Main.MODID);
    public static final RegistryObject<Item> ITEM_ALTAR_BLOCK = ITEMS.register("itemaltar", 
    () -> new BlockItem(Blocks.ITEM_ALTAR.get(), new Item.Properties()));

    public static final RegistryObject<Item> ARCANE_WAND = ITEMS.register("arcanewand", 
    () -> new Item(new Item.Properties().durability(300)));

    public static void registerItems(IEventBus bus){
        ITEMS.register(bus);
    }
}

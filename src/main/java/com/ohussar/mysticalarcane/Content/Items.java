package com.ohussar.mysticalarcane.Content;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.ArcaneWand.ArcaneWand;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, 
    Main.MODID);
    public static final RegistryObject<Item> ITEM_ALTAR_BLOCK = ITEMS.register("item_altar", 
    () -> new BlockItem(ModBlocks.ITEM_ALTAR.get(), new Item.Properties()));

    public static final RegistryObject<Item> ARCANE_WAND = ITEMS.register("arcanewand", 
    () -> new ArcaneWand(new Item.Properties().durability(300)));
    public static final RegistryObject<Item> MANA_ORCHID = ITEMS.register("mana_orchid", 
    () -> new BlockItem(ModBlocks.MANA_ORCHID.get(), new Item.Properties()));
    public static final RegistryObject<Item> MANA_INGOT = ITEMS.register("mana_ingot",
    () -> new Item(new Item.Properties()));
    public static void registerItems(IEventBus bus){
        ITEMS.register(bus);
    }
}

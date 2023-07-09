package com.ohussar.mysticalarcane.Content;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.ArcaneWand.ArcaneWand;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, 
    Main.MODID);
    public static final RegistryObject<Item> ITEM_ALTAR_BLOCK = ITEMS.register("item_altar", 
    () -> new BlockItem(ModBlocks.ITEM_ALTAR.get(), new Item.Properties().tab(Main.TAB)));
    public static final RegistryObject<Item> TANK_BLOCK = ITEMS.register("tank", 
    () -> new BlockItem(ModBlocks.TANK.get(), new Item.Properties().tab(Main.TAB)));
    public static final RegistryObject<Item> MANA_ORCHID = ITEMS.register("mana_orchid", 
    () -> new BlockItem(ModBlocks.MANA_ORCHID.get(), new Item.Properties().tab(Main.TAB)));
    public static final RegistryObject<Item> HOLDER = ITEMS.register("holder", 
    () -> new BlockItem(ModBlocks.HOLDER.get(), new Item.Properties().tab(Main.TAB)));
    public static final RegistryObject<Item> MANA_RECEPTOR = ITEMS.register("mana_receptor", 
    () -> new BlockItem(ModBlocks.MANA_RECEPTOR.get(), new Item.Properties().tab(Main.TAB)));

    public static final RegistryObject<Item> ARCANE_WAND = ITEMS.register("arcane_wand", 
    () -> new ArcaneWand(new Item.Properties().durability(300).tab(Main.TAB)));

    

    public static final RegistryObject<Item> MANA_INGOT = ITEMS.register("mana_ingot",
    () -> new Item(new Item.Properties().tab(Main.TAB)));
    public static void registerItems(IEventBus bus){
        ITEMS.register(bus);
    }
}

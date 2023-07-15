package com.ohussar.mysticalarcane.Base;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.Blocks.Holder.HolderMenu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MODID);
    
    
    public static RegistryObject<MenuType<HolderMenu>> HOLDER_MENU = register(HolderMenu::new, "holder_menu");

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(IContainerFactory<T> factory, String name){
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus bus){
        MENU_TYPES.register(bus);
    }

}

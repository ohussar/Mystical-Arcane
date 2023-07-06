package com.ohussar.mysticalarcane.Base;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.Recipes.ItemAltarRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MODID);

    public static final RegistryObject<RecipeSerializer<ItemAltarRecipe>> MANA_INFUSING_SERIALIZER = 
    RECIPE_SERIALIZER.register("mana_infusing", () -> ItemAltarRecipe.Serializer.INSTANCE);

    public static void registerRecipes(IEventBus bus){
        RECIPE_SERIALIZER.register(bus);
    }

}

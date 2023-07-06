package com.ohussar.mysticalarcane.Content.Recipes;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ohussar.mysticalarcane.Main;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class ItemAltarRecipe implements Recipe<SimpleContainer>{
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;


    public ItemAltarRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems){
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if(level.isClientSide()){
            return false;
        }
        return recipeItems.get(0).test(container.getItem(0)); 
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    public static class Type implements RecipeType<ItemAltarRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "mana_infusing";
    }

    public static class Serializer implements RecipeSerializer<ItemAltarRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Main.MODID, "mana_infusing");
        @Override
        public ItemAltarRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output")); 
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject,"ingredients");
            
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            if(ingredients.size() > 1){
                throw new IllegalArgumentException(String.format("Invalid recipe {}, expected only 1 ingredient, got {}", resourceLocation.getPath(), ingredients.size()));
            }

            inputs.set(0, Ingredient.fromJson(ingredients.get(0)));


            return new ItemAltarRecipe(resourceLocation, output, inputs);

        }

        @Override
        public @Nullable ItemAltarRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            if(inputs.size() > 1){
                throw new IllegalArgumentException(String.format("Invalid recipe {}, expected only 1 ingredient, got {}", resourceLocation.getPath(), inputs.size()));
            }

            ItemStack output = buf.readItem();
            return new ItemAltarRecipe(resourceLocation, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemAltarRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()){
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}

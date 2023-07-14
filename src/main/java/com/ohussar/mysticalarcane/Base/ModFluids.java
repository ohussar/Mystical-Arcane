package com.ohussar.mysticalarcane.Base;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.ModItems;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = 
    DeferredRegister.create(ForgeRegistries.FLUIDS, Main.MODID);


    public static final RegistryObject<FlowingFluid> SOURCE_MANA_WATER = 
    FLUIDS.register("mana_water_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.MANA_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_MANA_WATER = 
    FLUIDS.register("flowing_mana_water", () -> new ForgeFlowingFluid.Flowing(ModFluids.MANA_WATER_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties MANA_WATER_FLUID_PROPERTIES = 
    new ForgeFlowingFluid.Properties(ModFluidTypes.MANA_WATER_FLUID_TYPE, SOURCE_MANA_WATER, FLOWING_MANA_WATER).
        slopeFindDistance(1).levelDecreasePerBlock(2)
        .block(ModBlocks.MANA_WATER_BLOCK).bucket(ModItems.MANA_WATER_BUCKET);


    public static void register(IEventBus bus){
        FLUIDS.register(bus);
    }
}

package com.ohussar.mysticalarcane.Base;

import com.mojang.math.Vector3f;
import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.Fluids.BaseFluidType;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new
        ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new
        ResourceLocation("block/water_flow");
    public static final ResourceLocation MANA_OVERLAY_RL = new 
        ResourceLocation(Main.MODID, "blocks/mana_water_overlay");

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, 
    Main.MODID);

    public static final RegistryObject<FluidType> MANA_WATER_FLUID_TYPE = register("mana_water_fluid", 
    FluidType.Properties.create().lightLevel(10).density(20).viscosity(8));

    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties){
        return FLUID_TYPES.register(name, () -> new BaseFluidType(properties, WATER_STILL_RL, WATER_FLOWING_RL, MANA_OVERLAY_RL, 
        0xA1fa4545, new Vector3f(250f/255f, 69f/255f, 69f/255f)));
    }

    public static void register(IEventBus eventBus){
        FLUID_TYPES.register(eventBus);
    }


}

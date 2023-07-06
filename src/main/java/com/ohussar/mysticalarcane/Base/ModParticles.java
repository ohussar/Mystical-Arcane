package com.ohussar.mysticalarcane.Base;

import com.ohussar.mysticalarcane.Main;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = 
    DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Main.MODID);

    public static final RegistryObject<SimpleParticleType> MANA_PARTICLE = 
    PARTICLE_TYPES.register("mana_particle", () -> new SimpleParticleType(true));

    public static void RegisterParticleTypes(IEventBus bus){
        PARTICLE_TYPES.register(bus);
    }
}

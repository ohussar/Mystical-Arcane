package com.ohussar.mysticalarcane.Base;

import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Content.ArcaneWand.Projectile.WandProjectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MODID);
    public static final RegistryObject<EntityType<WandProjectile>> WAND_PROJECTILE = 
        ENTITIES.register("wandprojectile", () -> EntityType.Builder.of(
            (EntityType.EntityFactory<WandProjectile>) WandProjectile::new, MobCategory.MISC)
            .sized(0.5f, 0.5f).build("wandprojectile"));

    public static void registerEntitiesTypes(IEventBus bus){
        ENTITIES.register(bus);
    }

}

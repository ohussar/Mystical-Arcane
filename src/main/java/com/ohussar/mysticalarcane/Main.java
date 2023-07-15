package com.ohussar.mysticalarcane;

import com.mojang.logging.LogUtils;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Base.ModEntities;
import com.ohussar.mysticalarcane.Base.ModFluidTypes;
import com.ohussar.mysticalarcane.Base.ModFluids;
import com.ohussar.mysticalarcane.Base.ModMenus;
import com.ohussar.mysticalarcane.Base.ModParticles;
import com.ohussar.mysticalarcane.Base.ModRecipes;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.ModItems;
import com.ohussar.mysticalarcane.Content.Blocks.Holder.HolderEntityRenderer;
import com.ohussar.mysticalarcane.Content.Blocks.Holder.HolderScreen;
import com.ohussar.mysticalarcane.Content.Blocks.ItemAltar.ItemAltarBlockEntityRender;
import com.ohussar.mysticalarcane.Content.Blocks.Tank.TankEntityRender;
import com.ohussar.mysticalarcane.Content.Particles.ManaParticle;
import com.ohussar.mysticalarcane.Networking.ModMessages;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;
// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mysticalarcane";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab TAB = new CreativeModeTab("mysticalarcane") {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(ModItems.ARCANE_WAND.get());
        }
    };

    public Main()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModBlocks.registerBlocks(modEventBus);
        LOGGER.info("Registered blocks from " + MODID + "!");
        ModItems.registerItems(modEventBus);
        LOGGER.info("Registered items from " + MODID + "!");
        ModBlockEntities.registerBlockEntitiesTypes(modEventBus);
        LOGGER.info("Registered block entities types from " + MODID + "!");
        ModEntities.registerEntitiesTypes(modEventBus);
        LOGGER.info("Registered entities types from " + MODID + "!");
        ModParticles.RegisterParticleTypes(modEventBus);
        ModRecipes.registerRecipes(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModMenus.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(ModBlocks.MANA_ORCHID.getId(), () -> ModBlocks.POTTED_MANA_ORCHID.get());
            ModMessages.register();
        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(ModBlockEntities.ITEM_ALTAR_ENTITY.get(), ItemAltarBlockEntityRender::new);
            event.registerBlockEntityRenderer(ModBlockEntities.TANK_ENTITY.get(), TankEntityRender::new);
            event.registerBlockEntityRenderer(ModBlockEntities.HOLDER_ENTITY.get(), HolderEntityRenderer::new);
            //event.registerEntityRenderer(ModEntities.WAND_PROJECTILE.get(), WandProjectileRenderer::new);
        }

        @SubscribeEvent
        public static void registerParticleFactories(final RegisterParticleProvidersEvent event){
            event.register(ModParticles.MANA_PARTICLE.get(), ManaParticle.Provider::new);
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_MANA_WATER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_MANA_WATER.get(), RenderType.translucent());
            MenuScreens.register(ModMenus.HOLDER_MENU.get(), HolderScreen::new);
        }

    }
}

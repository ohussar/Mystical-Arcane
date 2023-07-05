package com.ohussar.mysticalarcane;

import com.mojang.logging.LogUtils;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Base.ModEntities;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.Items;
import com.ohussar.mysticalarcane.Content.ArcaneWand.Projectile.WandProjectileRenderer;
import com.ohussar.mysticalarcane.Content.ItemAltar.ItemAltarBlockEntityRender;
import com.ohussar.mysticalarcane.Networking.ModMessages;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public Main()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModBlocks.registerBlocks(modEventBus);
        LOGGER.info("Registered blocks from " + MODID + "!");
        Items.registerItems(modEventBus);
        LOGGER.info("Registered items from " + MODID + "!");
        ModBlockEntities.registerBlockEntitiesTypes(modEventBus);
        LOGGER.info("Registered block entities types from " + MODID + "!");
        ModEntities.registerEntitiesTypes(modEventBus);
        LOGGER.info("Registered entities types from " + MODID + "!");
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(ModBlockEntities.ITEM_ALTAR_ENTITY.get(), ItemAltarBlockEntityRender::new);
            event.registerEntityRenderer(ModEntities.WAND_PROJECTILE.get(), WandProjectileRenderer::new);
        }
    }
}

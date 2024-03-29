package com.ohussar.mysticalarcane.Networking;

import com.ohussar.mysticalarcane.Main;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Main.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncInventoryClient.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncInventoryClient::new)
                .encoder(SyncInventoryClient::toBytes)
                .consumerMainThread(SyncInventoryClient::handle)
                .add();
        net.messageBuilder(SyncHeightModelAltar.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncHeightModelAltar::new)
                .encoder(SyncHeightModelAltar::toBytes)
                .consumerMainThread(SyncHeightModelAltar::handle)
                .add();
        net.messageBuilder(SpawnParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpawnParticles::new)
                .encoder(SpawnParticles::toBytes)
                .consumerMainThread(SpawnParticles::handle)
                .add();
        net.messageBuilder(SyncVariables.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncVariables::new)
                .encoder(SyncVariables::toBytes)
                .consumerMainThread(SyncVariables::handle)
                .add();
        net.messageBuilder(ButtonCall.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ButtonCall::new)
                .encoder(ButtonCall::toBytes)
                .consumerMainThread(ButtonCall::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
         INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}

package net.typho.config.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.typho.config.SetConfigValuePacket;

public class TyphoConfigClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SetConfigValuePacket.ID, (payload, context) -> {
            if (payload.option() != null) {
                payload.option().set(payload.value());
                payload.option().updatedClient(MinecraftClient.getInstance());
            }
        });
    }
}

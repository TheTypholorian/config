package net.typho.config;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class TyphoConfig implements ModInitializer {
    public static final String MOD_ID = "typho_config";
    public static final Map<Identifier, ConfigOption<?>> ALL_OPTIONS = new LinkedHashMap<>();
    public static final Map<Identifier, RootConfigOptionGroup> ROOT_OPTION_GROUPS = new LinkedHashMap<>();

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SetConfigValuePacket.ID, SetConfigValuePacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SetConfigValuePacket.ID, SetConfigValuePacket.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetConfigValuePacket.ID, (payload, context) -> {
            if (payload.option() != null && context.player().hasPermissionLevel(2)) {
                payload.option().set(payload.value());
                payload.option().updatedServer(context.server());
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                for (ConfigOption<?> option : ALL_OPTIONS.values()) {
                    if (option.env == EnvType.SERVER) {
                        sender.sendPacket(new SetConfigValuePacket<>(option));
                    }
                }
            }
        });
    }

    public static void read(Dynamic<?> dynamic) {
        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            if (option.env == EnvType.SERVER) {
                dynamic.get(option.id.toString()).get().ifSuccess(result -> {
                    DataResult<? extends Pair<?, ?>> result1 = option.codec().decode(result);

                    if (result1.isSuccess()) {
                        option.set(result1.getOrThrow().getFirst());
                    } else {
                        System.err.println("Error reading server config [" + option.id + "]: " + result1.error().orElseThrow().message());
                    }
                });
            }
        }
    }

    private static <T> void write(ConfigOption<T> option, NbtCompound nbt) {
        if (option.env == EnvType.SERVER && (option.defValue == null || !option.defValue.equals(option.value))) {
            option.codec().encodeStart(NbtOps.INSTANCE, option.get())
                    .result().ifPresent(elem -> nbt.put(option.id.toString(), elem));
        }
    }

    public static NbtElement write() {
        NbtCompound nbt = new NbtCompound();

        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            write(option, nbt);
        }

        return nbt;
    }
}

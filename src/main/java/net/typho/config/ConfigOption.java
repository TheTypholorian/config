package net.typho.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigOption<O> implements ConfigOptionGroupChild, SimpleOption.TooltipFactory<O> {
    public final Identifier id;
    public final ConfigOptionGroup parent;
    public final ArgumentType<O> argumentType;
    public final O defValue;
    public final EnvType env;
    protected O value;

    protected ConfigOption(EnvType env, ConfigOptionGroup parent, Identifier id, ArgumentType<O> argumentType, O value) {
        this.env = env;
        this.id = id;
        this.parent = parent;
        this.argumentType = argumentType;
        this.value = defValue = value;
        parent.options.add(this);

        if (TyphoConfig.ALL_OPTIONS.put(id, this) != null) {
            throw new IllegalStateException("Duplicate option " + id);
        }
    }

    public interface Builder<B extends Builder<B, O, T>, O, T extends ConfigOption<O>> {
        B parent(ConfigOptionGroup parent);

        B env(EnvType env);

        B id(String folder);

        B id(Identifier id);

        B value(O value);

        T build();
    }

    public String valueAsString() {
        return get().toString();
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public @Nullable Tooltip apply(O value) {
        return null;
    }

    @Override
    public EnvType env() {
        return env;
    }

    public O get() {
        return value;
    }

    public void updatedClient() {
        updatedClient(MinecraftClient.getInstance());
    }

    public void updatedClient(MinecraftClient client) {
    }

    public void updatedServer(MinecraftServer server) {
    }

    public boolean set(NbtElement nbt) {
        return set(codec().decode(NbtOps.INSTANCE, nbt));
    }

    @SuppressWarnings("unchecked")
    public boolean set(Object value) {
        this.value = (O) value;
        return true;
    }

    public void setUpdateSendClient(Object value) {
        if (env == EnvType.SERVER) {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null && client.player.hasPermissionLevel(2) && set(value)) {
                updatedClient(client);
                client.player.networkHandler.sendPacket(new CustomPayloadC2SPacket(new SetConfigValuePacket<>(this, get())));
            }
        } else {
            set(value);
        }
    }

    public void set(String s) {
        try {
            set(argumentType.parse(new StringReader(s)));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String translationKey() {
        return id.toTranslationKey("config").replace('/', '.');
    }

    public abstract Codec<O> codec();

    public abstract PacketCodec<ByteBuf, O> packetCodec();

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + id.toString() + " = " + get().toString();
    }
}

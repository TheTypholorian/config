package net.typho.config;

import net.fabricmc.api.EnvType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetConfigValuePacket<T>(ConfigOption<T> option, T value) implements CustomPayload {
    public SetConfigValuePacket(ConfigOption<T> option) {
        this(option, option.value);
    }

    @SuppressWarnings("unchecked")
    public static <T> SetConfigValuePacket<T> cast(ConfigOption<?> option, Object value) {
        return new SetConfigValuePacket<>((ConfigOption<T>) option, (T) value);
    }

    public void encode(PacketByteBuf buf) {
        Identifier.PACKET_CODEC.encode(buf, option.id);
        option.packetCodec().encode(buf, value);
    }

    public static final Id<SetConfigValuePacket<?>> ID = new Id<>(Identifier.of(TyphoConfig.MOD_ID, "set_value"));
    public static final PacketCodec<PacketByteBuf, SetConfigValuePacket<?>> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public SetConfigValuePacket<?> decode(PacketByteBuf buf) {
            Identifier id = Identifier.PACKET_CODEC.decode(buf);
            ConfigOption<?> option = TyphoConfig.ALL_OPTIONS.get(id);

            if (option == null) {
                throw new NullPointerException("No feature with id " + id);
            }

            SetConfigValuePacket<?> packet = SetConfigValuePacket.cast(option, option.packetCodec().decode(buf));

            if (option.env == EnvType.CLIENT) {
                System.err.println("Something went wrong and client-side option " + id + " was sent to client?");
                return new SetConfigValuePacket<>(null, null);
            }

            return packet;
        }

        @Override
        public void encode(PacketByteBuf buf, SetConfigValuePacket<?> packet) {
            packet.encode(buf);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

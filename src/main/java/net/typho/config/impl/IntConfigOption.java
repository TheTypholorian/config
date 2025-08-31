package net.typho.config.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.config.ConfigOption;
import net.typho.config.ConfigOptionGroup;
import net.typho.config.client.ConfigScreen;

public class IntConfigOption extends ConfigOption<Integer> {
    public final ItemStack icon;

    protected IntConfigOption(EnvType env, ConfigOptionGroup parent, Identifier id, Integer value, ItemStack icon) {
        super(env, parent, id, IntegerArgumentType.integer(), value);
        this.icon = icon == null ? ItemStack.EMPTY : icon;
    }

    public static class Builder implements ConfigOption.Builder<Builder, Integer, IntConfigOption> {
        public EnvType env;
        public ConfigOptionGroup parent;
        public Identifier id;
        public int value;
        public ItemStack icon;

        @Override
        public Builder parent(ConfigOptionGroup parent) {
            this.parent = parent;
            this.env = parent.env;
            return this;
        }

        @Override
        public Builder env(EnvType env) {
            this.env = env;
            return this;
        }

        @Override
        public Builder id(String folder) {
            id(parent.id.withSuffixedPath("/" + folder));
            return this;
        }

        @Override
        public Builder id(Identifier id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder value(Integer value) {
            this.value = value;
            return this;
        }

        @Override
        public IntConfigOption build() {
            return new IntConfigOption(env, parent, id, value, icon);
        }
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public boolean scroll(ConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        setUpdateSendClient(get() + (int) verticalAmount);
        return true;
    }

    @Override
    public void render(ConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = Text.literal(get().toString());
        context.drawTextWithShadow(node.textRenderer(), text, node.getX() + node.getWidth() - node.textRenderer().getWidth(text) - 8, node.getY() + (node.getHeight() - node.textRenderer().fontHeight) / 2, 0xFFFFFFFF);
    }

    @Override
    public Codec<Integer> codec() {
        return Codec.INT;
    }

    @Override
    public PacketCodec<ByteBuf, Integer> packetCodec() {
        return PacketCodecs.INTEGER;
    }
}

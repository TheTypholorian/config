package net.typho.config.impl;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.typho.config.ConfigOption;
import net.typho.config.ConfigOptionGroup;
import net.typho.config.client.ConfigScreen;

public class FloatConfigOption extends ConfigOption<Float> {
    public final ItemStack icon;

    public FloatConfigOption(ConfigOptionGroup parent, String name, Float value, ItemStack icon) {
        super(parent, name, FloatArgumentType.floatArg(), value);
        this.icon = icon;
    }

    public FloatConfigOption(EnvType env, ConfigOptionGroup parent, String name, Float value, ItemStack icon) {
        super(env, parent, name, FloatArgumentType.floatArg(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public boolean scroll(ConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        setUpdateSendClient(get() + (float) verticalAmount / 10f);
        return true;
    }

    @Override
    public void render(ConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = Text.literal(String.format("%.1f", get()));
        context.drawTextWithShadow(node.textRenderer(), text, node.getX() + node.getWidth() - node.textRenderer().getWidth(text) - 8, node.getY() + (node.getHeight() - node.textRenderer().fontHeight) / 2, 0xFFFFFFFF);
    }

    @Override
    public SimpleOption<Float> asSimpleOption() {
        throw new UnsupportedOperationException("Can't create a client option for a boundless float");
    }

    @Override
    public Codec<Float> codec() {
        return Codec.FLOAT;
    }

    @Override
    public PacketCodec<ByteBuf, Float> packetCodec() {
        return PacketCodecs.FLOAT;
    }
}

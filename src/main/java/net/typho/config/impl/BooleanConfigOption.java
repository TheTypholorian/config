package net.typho.config.impl;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.typho.config.ConfigOption;
import net.typho.config.ConfigOptionGroup;
import net.typho.config.client.ConfigScreen;

public class BooleanConfigOption extends ConfigOption<Boolean> {
    public final ItemStack icon;

    public BooleanConfigOption(ConfigOptionGroup parent, String name, Boolean value, ItemStack icon) {
        super(parent, name, BoolArgumentType.bool(), value);
        this.icon = icon;
    }

    public BooleanConfigOption(EnvType env, ConfigOptionGroup parent, String name, Boolean value, ItemStack icon) {
        super(env, parent, name, BoolArgumentType.bool(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ConfigScreen.Node node, double mouseX, double mouseY) {
        setUpdateSendClient(!get());
    }

    @Override
    public boolean scroll(ConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        click(node, mouseX, mouseY);
        return true;
    }

    @Override
    public void render(ConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = get() ? ScreenTexts.ON : ScreenTexts.OFF;
        context.drawTextWithShadow(node.textRenderer(), text, node.getX() + node.getWidth() - node.textRenderer().getWidth(text) - 8, node.getY() + (node.getHeight() - node.textRenderer().fontHeight) / 2, 0xFFFFFFFF);
    }

    @Override
    public Codec<Boolean> codec() {
        return Codec.BOOL;
    }

    @Override
    public PacketCodec<ByteBuf, Boolean> packetCodec() {
        return PacketCodecs.BOOL;
    }
}

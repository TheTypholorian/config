package net.typho.config;

import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.config.client.ConfigScreen;

public class RootConfigOptionGroup extends ConfigOptionGroup {
    protected RootConfigOptionGroup(EnvType env, ItemStack icon, Identifier id) {
        super(env, icon, id, null);

        if (TyphoConfig.ROOT_OPTION_GROUPS.put(this.id, this) != null) {
            throw new IllegalStateException("Duplicate root group " + id);
        }
    }

    public static class Builder {
        public EnvType env;
        public Identifier id;
        public ItemStack icon;

        public Builder env(EnvType env) {
            this.env = env;
            return this;
        }

        public Builder id(Identifier id) {
            this.id = id;
            return this;
        }

        public Builder icon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public RootConfigOptionGroup build() {
            return new RootConfigOptionGroup(env, icon, id);
        }
    }

    public void addButton(DirectionalLayoutWidget layout) {
        layout.add(new Button());
    }

    public class Button extends ButtonWidget {
        public Button() {
            super(0, 0, DEFAULT_HEIGHT, DEFAULT_HEIGHT, Text.empty(), button -> MinecraftClient.getInstance().setScreen(new ConfigScreen(name(), RootConfigOptionGroup.this, MinecraftClient.getInstance().currentScreen)), DEFAULT_NARRATION_SUPPLIER);
            setTooltip(Tooltip.of(name()));
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
            context.drawItem(icon(), getX() + (getWidth() - 16) / 2, getY() + (getHeight() - 16) / 2);
        }
    }
}

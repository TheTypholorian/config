package net.typho.config;

import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.config.client.ConfigScreen;

public interface ConfigOptionGroupChild {
    EnvType env();

    Identifier id();

    default boolean enabled() {
        if (env() == EnvType.SERVER) {
            return MinecraftClient.getInstance().world != null;
        }

        return true;
    }

    default Text name() {
        return Text.literal(Text.translatable(id().toTranslationKey("config").replace('/', '.')).asTruncatedString(32));
    }

    ItemStack icon();

    default void init(ConfigScreen.Node node) {
    }

    default void click(ConfigScreen.Node node, double mouseX, double mouseY) {
    }

    default boolean scroll(ConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return false;
    }

    default void render(ConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
    }
}

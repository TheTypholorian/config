package net.typho.config;

import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
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

    default MutableText name() {
        return Text.translatable(id().toTranslationKey("config").replace('/', '.'));
    }

    default MutableText desc() {
        String key = id().toTranslationKey("config", "desc").replace('/', '.');

        if (!Language.getInstance().hasTranslation(key)) {
            return null;
        } else {
            return Text.translatable(key);
        }
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

package net.typho.config;

import com.google.common.collect.Iterators;
import net.fabricmc.api.EnvType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.typho.config.client.ConfigScreen;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConfigOptionGroup implements ConfigOptionGroupChild, Iterable<ConfigOptionGroupChild> {
    public final Identifier id;
    public final ConfigOptionGroup parent;
    public final List<ConfigOptionGroup> subGroups = new LinkedList<>();
    public final List<ConfigOption<?>> options = new LinkedList<>();
    public final ItemStack icon;
    public final EnvType env;

    protected ConfigOptionGroup(EnvType env, ItemStack icon, Identifier id, ConfigOptionGroup parent) {
        this.env = env;
        this.icon = icon;
        this.id = id;
        this.parent = parent;

        if (parent != null) {
            parent.subGroups.add(this);
        }
    }

    public static class Builder {
        public EnvType env;
        public ConfigOptionGroup parent;
        public Identifier id;
        public ItemStack icon;

        public Builder parent(ConfigOptionGroup parent) {
            this.parent = parent;
            this.env = parent.env;
            return this;
        }

        public Builder env(EnvType env) {
            this.env = env;
            return this;
        }

        public Builder id(String folder) {
            id(parent.id.withSuffixedPath("/" + folder));
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

        public ConfigOptionGroup build() {
            return new ConfigOptionGroup(env, icon, id, parent);
        }
    }

    @Override
    public @NotNull Iterator<ConfigOptionGroupChild> iterator() {
        return Iterators.concat(subGroups.iterator(), options.iterator());
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public EnvType env() {
        return env;
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public void click(ConfigScreen.Node node, double mouseX, double mouseY) {
        node.parent().pushTab(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName() + " " + id.toString());

        for (ConfigOptionGroup subGroup : subGroups) {
            builder.append('\n').append(subGroup);
        }

        for (ConfigOption<?> option : options) {
            builder.append('\n').append(option);
        }

        return builder.toString();
    }
}

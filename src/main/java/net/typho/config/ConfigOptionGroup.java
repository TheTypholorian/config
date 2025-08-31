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
    public final String name;
    public final ConfigOptionGroup parent;
    public final List<ConfigOptionGroup> subGroups = new LinkedList<>();
    public final List<ConfigOption<?>> options = new LinkedList<>();
    public final ItemStack icon;
    public final EnvType env;

    public ConfigOptionGroup(EnvType env, ItemStack icon, Identifier id) {
        this(env, icon, id, id.getPath());
    }

    public ConfigOptionGroup(EnvType env, ItemStack icon, Identifier id, String name) {
        this.env = env;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.parent = null;
    }

    public ConfigOptionGroup(EnvType env, ItemStack icon, ConfigOptionGroup parent, String folder) {
        this.env = env;
        this.icon = icon;
        this.id = parent.id.withSuffixedPath("/" + folder);
        this.name = folder;
        this.parent = parent;

        parent.subGroups.add(this);
    }

    public ConfigOptionGroup(ItemStack icon, ConfigOptionGroup parent, String folder) {
        this(parent.env, icon, parent, folder);
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

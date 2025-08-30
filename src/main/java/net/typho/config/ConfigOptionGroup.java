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

    @SuppressWarnings("unchecked")
    public <O> ConfigOption<O> getFeature(String name) {
        return (ConfigOption<O>) options.stream().filter(feature -> feature.name.equals(name)).findAny().orElse(null);
    }

    public ConfigOptionGroup getFolder(String name) {
        return subGroups.stream().filter(group -> group.name.equals(name)).findAny().orElse(null);
    }

    public <O> ConfigOption<O> getFeature(String... path) {
        if (path.length == 1) {
            return getFeature(path[0]);
        }

        ConfigOptionGroup found = getFolder(path[0]);

        if (found == null) {
            return null;
        }

        String[] subPath = new String[path.length - 1];
        System.arraycopy(path, 1, subPath, 0, subPath.length);
        return found.getFeature(subPath);
    }

    public ConfigOptionGroup getFolder(String... path) {
        if (path.length == 1) {
            return getFolder(path[0]);
        }

        ConfigOptionGroup found = subGroups.stream().filter(group -> group.name.equals(path[0])).findAny().orElse(null);

        if (found == null) {
            return null;
        }

        String[] subPath = new String[path.length - 1];
        System.arraycopy(path, 1, subPath, 0, subPath.length);
        return found.getFolder(subPath);
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

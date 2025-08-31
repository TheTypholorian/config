package net.typho.config.impl;

import net.fabricmc.api.EnvType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.typho.config.ConfigOption;
import net.typho.config.ConfigOptionGroup;

public class RangedIntConfigOption extends IntConfigOption {
    public final int min, max;

    protected RangedIntConfigOption(EnvType env, ConfigOptionGroup parent, Identifier id, Integer value, ItemStack icon, int min, int max) {
        super(env, parent, id, value, icon);
        this.min = min;
        this.max = max;
    }

    public static class Builder implements ConfigOption.Builder<Builder, Integer, RangedIntConfigOption> {
        public EnvType env;
        public ConfigOptionGroup parent;
        public Identifier id;
        public int value, min, max;
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
        public Builder icon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public Builder max(int max) {
            this.max = max;
            return this;
        }

        @Override
        public RangedIntConfigOption build() {
            return new RangedIntConfigOption(env, parent, id, value, icon, min, max);
        }
    }

    @Override
    public boolean set(Object value) {
        int clamped = MathHelper.clamp((int) value, min, max);
        super.set(clamped);
        return !value.equals(clamped);
    }
}

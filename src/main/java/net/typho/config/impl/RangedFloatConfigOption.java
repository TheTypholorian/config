package net.typho.config.impl;

import net.fabricmc.api.EnvType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.typho.config.ConfigOption;
import net.typho.config.ConfigOptionGroup;

public class RangedFloatConfigOption extends FloatConfigOption {
    public final float min, max;

    protected RangedFloatConfigOption(EnvType env, ConfigOptionGroup parent, Identifier id, Float value, ItemStack icon, float min, float max) {
        super(env, parent, id, value, icon);
        this.min = min;
        this.max = max;
    }

    public static class Builder implements ConfigOption.Builder<Builder, Float, RangedFloatConfigOption> {
        public EnvType env;
        public ConfigOptionGroup parent;
        public Identifier id;
        public float value, min, max;
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
        public Builder value(Float value) {
            this.value = value;
            return this;
        }

        public Builder min(Float min) {
            this.min = min;
            return this;
        }

        public Builder max(Float max) {
            this.max = max;
            return this;
        }

        @Override
        public RangedFloatConfigOption build() {
            return new RangedFloatConfigOption(env, parent, id, value, icon, min, max);
        }
    }

    @Override
    public boolean set(Object value) {
        float clamped = MathHelper.clamp((float) value, min, max);
        super.set(clamped);
        return !value.equals(clamped);
    }
}

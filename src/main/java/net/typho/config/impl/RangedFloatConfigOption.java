package net.typho.config.impl;

import net.fabricmc.api.EnvType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.typho.config.ConfigOptionGroup;

public class RangedFloatConfigOption extends FloatConfigOption {
    public final float min, max;

    public RangedFloatConfigOption(ConfigOptionGroup parent, String name, Float value, ItemStack icon, float min, float max) {
        super(parent, name, value, icon);
        this.min = min;
        this.max = max;
    }

    public RangedFloatConfigOption(EnvType env, ConfigOptionGroup parent, String name, Float value, ItemStack icon, float min, float max) {
        super(env, parent, name, value, icon);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean set(Object value) {
        float clamped = MathHelper.clamp((float) value, min, max);
        super.set(clamped);
        return !value.equals(clamped);
    }
}

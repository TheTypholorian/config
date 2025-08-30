package net.typho.config.impl;

import net.fabricmc.api.EnvType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.typho.config.ConfigOptionGroup;

public class RangedIntConfigOption extends IntConfigOption {
    public final int min, max;

    public RangedIntConfigOption(ConfigOptionGroup parent, String name, Integer value, ItemStack icon, int min, int max) {
        super(parent, name, value, icon);
        this.min = min;
        this.max = max;
    }

    public RangedIntConfigOption(EnvType env, ConfigOptionGroup parent, String name, Integer value, ItemStack icon, int min, int max) {
        super(env, parent, name, value, icon);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean set(Object value) {
        int clamped = MathHelper.clamp((int) value, min, max);
        super.set(clamped);
        return !value.equals(clamped);
    }
}

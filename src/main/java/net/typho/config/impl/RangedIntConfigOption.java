package net.typho.config.impl;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.typho.config.ConfigOptionGroup;

import java.util.Optional;

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
    public SimpleOption<Integer> asSimpleOption() {
        SimpleOption<Integer> option = new SimpleOption<>(
                translationKey(),
                this,
                GameOptions::getGenericValueText,
                new SimpleOption.SliderCallbacks<>() {
                    @Override
                    public double toSliderProgress(Integer i) {
                        return (double) (i - min) / max;
                    }

                    @Override
                    public Integer toValue(double d) {
                        return (int) (d * (max - min) + min);
                    }

                    @Override
                    public Optional<Integer> validate(Integer value) {
                        return value >= 0 && value <= 1 ? Optional.of(value) : Optional.empty();
                    }

                    @Override
                    public Codec<Integer> codec() {
                        return Codec.INT;
                    }
                },
                defValue,
                this::set
        );
        option.setValue(value);
        return option;
    }

    @Override
    public boolean set(Object value) {
        int clamped = MathHelper.clamp((int) value, min, max);
        super.set(clamped);
        return !value.equals(clamped);
    }
}

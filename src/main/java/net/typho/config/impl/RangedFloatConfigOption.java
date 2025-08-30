package net.typho.config.impl;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.typho.config.ConfigOptionGroup;

import java.util.Optional;

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
    public SimpleOption<Float> asSimpleOption() {
        SimpleOption<Float> option = new SimpleOption<>(
                translationKey(),
                this,
                (text, value) -> GameOptions.getGenericValueText(text, Text.of(String.format("%.1f", value))),
                new SimpleOption.SliderCallbacks<>() {
                    @Override
                    public double toSliderProgress(Float f) {
                        return (f - min) / max;
                    }

                    @Override
                    public Float toValue(double d) {
                        return (float) (d * (max - min) + min);
                    }

                    @Override
                    public Optional<Float> validate(Float value) {
                        return value >= 0 && value <= 1 ? Optional.of(value) : Optional.empty();
                    }

                    @Override
                    public Codec<Float> codec() {
                        return Codec.FLOAT;
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
        float clamped = MathHelper.clamp((float) value, min, max);
        super.set(clamped);
        return !value.equals(clamped);
    }
}

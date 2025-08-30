package net.typho.config.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.minecraft.client.option.GameOptions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.typho.config.ConfigOption;
import net.typho.config.TyphoConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintWriter;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(
            method = "write",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/GameOptions;accept(Lnet/minecraft/client/option/GameOptions$Visitor;)V"
            )
    )
    private void write(CallbackInfo ci, @Local PrintWriter out) {
        for (ConfigOption<?> option : TyphoConfig.ALL_OPTIONS.values()) {
            if (option.env == EnvType.CLIENT) {
                out.println(option.id.toString().replace(':', '/') + ":" + option.valueAsString());
            }
        }
    }

    @Inject(
            method = "load",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/GameOptions;accept(Lnet/minecraft/client/option/GameOptions$Visitor;)V"
            )
    )
    private void load(CallbackInfo ci, @Local(ordinal = 1) NbtCompound nbt) {
        for (ConfigOption<?> option : TyphoConfig.ALL_OPTIONS.values()) {
            if (option.env == EnvType.CLIENT) {
                NbtElement input = nbt.get(option.id.toString().replace(':', '/'));

                if (input != null) {
                    option.set(input);
                }
            }
        }
    }
}

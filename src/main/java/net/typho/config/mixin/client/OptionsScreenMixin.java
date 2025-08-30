package net.typho.config.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.typho.config.RootConfigOptionGroup;
import net.typho.config.TyphoConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {
    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;",
                    ordinal = 0
            )
    )
    private void init(CallbackInfo ci, @Local(ordinal = 0) DirectionalLayoutWidget layout) {
        if (!TyphoConfig.ROOT_OPTION_GROUPS.isEmpty()) {
            DirectionalLayoutWidget layout1 = layout.add(DirectionalLayoutWidget.horizontal()).spacing(8);

            for (RootConfigOptionGroup group : TyphoConfig.ROOT_OPTION_GROUPS.values()) {
                group.addButton(layout1);
            }
        }
    }
}

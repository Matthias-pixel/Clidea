package de.ideaonic703.clidea.mixin.gui;

import de.ideaonic703.clidea.gui.screen.ClideaConfigScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        OptionsScreen thiz = (OptionsScreen)(Object)this;
        OptionsScreenAccessor thix = (OptionsScreenAccessor)thiz;
        this.addDrawableChild(new ButtonWidget(10, 10, 100, 20, Text.literal("Clidea"), (button) -> {
            assert this.client != null;
            this.client.setScreen(new ClideaConfigScreen(thiz, thix.getSettings()));
        }));
    }
}

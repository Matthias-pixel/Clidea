package de.ideaonic703.clidea.gui.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.ideaonic703.clidea.ClideaModules;
import de.ideaonic703.clidea.module.ClideaModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DialogScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ClideaConfigScreen extends GameOptionsScreen {
    private ButtonListWidget moduleList;
    private final GameOptions settings;
    public ClideaConfigScreen(Screen parent, GameOptions settings) {
        super(parent, settings, Text.literal("Clidea Options"));
        this.settings = settings;
    }

    @Override
    protected void init() {
        this.moduleList = new ButtonListWidget(this.client, this.width, this.height, /*top*/32, /*bottom*/this.height - 32, /*itemHeight*/25);
        String[] modules = ClideaModules.getInstance().getModules();
        for (int i = 0; i < modules.length; i++) {
            ClideaModule module = ClideaModules.getInstance().getModule(modules[i]);
            final int finalI = i;
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 35 + i * 25, 200, 20, module.getName(), button -> {
                assert this.client != null;
                this.client.setScreen(ClideaModules.getInstance().getScreen(this, this.settings, modules[finalI]));
            }));
        }
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, Text.literal("Done"), button -> {
            assert this.client != null;
            this.client.setScreen(this.parent);
        }));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        return this.moduleList.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.moduleList.render(matrices, mouseX, mouseY, delta);
        ClideaConfigScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0x7d32a8);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = ClideaConfigScreen.getHoveredButtonTooltip(this.moduleList, mouseX, mouseY);
        this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
    }
}

package de.ideaonic703.clidea.gui.screen;

import de.ideaonic703.clidea.ClideaModules;
import de.ideaonic703.clidea.module.ClideaModule;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Set;

public class ModuleConfigScreen extends GameOptionsScreen {
    private String id;
    private ButtonListWidget list;

    public ModuleConfigScreen(Screen parent, GameOptions settings, String id) {
        super(parent, settings, Text.literal(""));
        this.id = id;
    }

    @Override
    protected void init() {
        this.list = new ButtonListWidget(this.client, this.width, this.height, /*top*/32, /*bottom*/this.height - 32, /*itemHeight*/25);
        NbtCompound config = ClideaModules.getInstance().getModule(this.id).getConfig();
        String[] keys = config.getKeys().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            String settingId = keys[i];
            NbtCompound setting = (NbtCompound) config.get(settingId);
            assert setting != null;
            if(setting.contains("toggle")) {
                this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 35 + i * 25, 200, 20, Text.literal(String.format("%s: %s", setting.getString("name"), setting.getBoolean("toggle") ? "ON" : "OFF")), button -> {
                    ClideaModules.getInstance().getModule(id).changeSetting(settingId, !setting.getBoolean("toggle"));
                    NbtCompound current_setting = (NbtCompound) ClideaModules.getInstance().getModule(this.id).getConfig().get(settingId);
                    assert current_setting != null;
                    button.setMessage(Text.literal(String.format("%s: %s", current_setting.getString("name"), current_setting.getBoolean("toggle") ? "ON" : "OFF")));
                }));
            } else if(setting.contains("slider")) {
                final int start = setting.getInt("slider_start");
                final int end = setting.getInt("slider_end");
                final double range = end-start;
                final double ratio = (setting.getInt("slider")-start)/range;
                this.addDrawableChild(new SliderWidget(this.width / 2 - 100, 35 + i * 25, 200, 20, Text.literal(String.format("%s: %d", setting.getString("name"), setting.getInt("slider"))), ratio) {
                    @Override
                    protected void updateMessage() {
                        this.setMessage(Text.literal(String.format("%s: %d", setting.getString("name"), (int)(this.value*range+start))));
                    }

                    @Override
                    protected void applyValue() {
                        ClideaModules.getInstance().getModule(id).changeSetting(settingId, (int)(this.value*range+start));
                    }
                });
            }
        }
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, Text.literal("Apply"), button -> {
            ClideaModules.getInstance().saveConfigs();
            assert this.client != null;
            this.client.setScreen(this.parent);
        }));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        return this.list.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        ClideaConfigScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0x7d32a8);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = ClideaConfigScreen.getHoveredButtonTooltip(this.list, mouseX, mouseY);
        this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
    }
}
package de.ideaonic703.clidea;

import de.ideaonic703.clidea.gui.screen.ModuleConfigScreen;
import de.ideaonic703.clidea.module.ClideaModule;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;

import java.io.IOException;
import java.util.HashMap;

public class ClideaModules {
    private static ClideaModules instance;
    public static ClideaModules getInstance() {
        if(instance == null)
            instance = new ClideaModules();
        return instance;
    }

    private final HashMap<String, ClideaModule> modules;
    private NbtCompound config;

    private ClideaModules() {
        this.modules = new HashMap<>();
        try {
            config = NbtIo.read(Clidea.CONFIG_FILE);
        } catch (IOException e) {
            config = new NbtCompound();
        }
    }
    public void registerModule(ClideaModule module) {
        String id = module.getId();
        if(this.modules.containsKey(id))
            throw new RuntimeException(String.format("A module with this name already exists! (%s)", id));
        this.modules.put(id, module);
        if(this.config.contains(id))
            module.setConfig(this.config.getCompound(id));
    }
    public void saveConfigs() {
        for(ClideaModule module : this.modules.values()) {
            this.config.put(module.getId(), module.getConfig());
        }
        try {
            NbtIo.write(this.config, Clidea.CONFIG_FILE);
        } catch (IOException e) {
            Clidea.LOGGER.error("Couldn't save config changes!");
        }
    }
    public ClideaModule getModule(String id) {
        return this.modules.get(id);
    }
    public String[] getModules() {
        return this.modules.keySet().toArray(new String[0]);
    }

    public Screen getScreen(Screen parent, GameOptions settings, String id) {
        return new ModuleConfigScreen(parent, settings, id);
    }
}

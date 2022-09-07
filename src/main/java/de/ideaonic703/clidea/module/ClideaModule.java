package de.ideaonic703.clidea.module;

import de.ideaonic703.clidea.ClideaModules;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;

public interface ClideaModule {
    String getId();
    void setConfig(NbtCompound config);
    NbtCompound getConfig();
    Text getName();
    boolean changeSetting(String id, boolean element);
    int changeSetting(String id, int element);
}

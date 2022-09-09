package de.ideaonic703.clidea.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.swing.text.Style;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class PacketLoggerModule implements ClideaModule {
    private static final String ID = "PacketLoggerModule";
    private static PacketLoggerModule instance;
    public static PacketLoggerModule getInstance() {
        if(instance == null)
            instance = new PacketLoggerModule();
        return instance;
    }
    private static final NbtCompound DEFAULT_CONFIG;
    static {
        DEFAULT_CONFIG = new NbtCompound();
        NbtCompound enabled = new NbtCompound();
        enabled.putString("name", "Enabled");
        enabled.putBoolean("toggle", false);
        DEFAULT_CONFIG.put("enabled", enabled);
        NbtCompound outgoing = new NbtCompound();
        outgoing.putString("name", "Log outgoing Packets C2S");
        outgoing.putBoolean("toggle", true);
        DEFAULT_CONFIG.put("outgoing", outgoing);
        NbtCompound incoming = new NbtCompound();
        incoming.putString("name", "Log incoming Packets S2C");
        incoming.putBoolean("toggle", false);
        DEFAULT_CONFIG.put("incoming", incoming);
    }
    private final NbtCompound config = DEFAULT_CONFIG;
    private PacketLoggerModule() {}

    public boolean outgoingPacket(Packet<?> packet, PacketCallbacks callbacks) {
        Text message = Text.literal(String.format("[Server Logger C->] %s", packet.getClass().getSimpleName())).formatted(Formatting.LIGHT_PURPLE);
        MinecraftClient.getInstance().getMessageHandler().onGameMessage(message, false);
        return false;
    }
    public <T extends PacketListener> boolean incomingPacket(Packet<T> packet, PacketListener listener) {
        Text message = Text.literal(String.format("[Server Logger S->] %s", packet.getClass().getSimpleName())).formatted(Formatting.DARK_AQUA);
        MinecraftClient.getInstance().getMessageHandler().onGameMessage(message, false);
        return false;
    }

    @Override
    public String getId() {
        return ID;
    }
    @Override
    public void setConfig(NbtCompound config) {
        final String[] settingKeys = this.config.getKeys().toArray(new String[0]);
        for (String settingKey : settingKeys) {
            if (config.contains(settingKey)) {
                this.config.put(settingKey, config.get(settingKey));
            }
        }
    }
    @Override
    public NbtCompound getConfig() {
        return this.config;
    }
    @Override
    public Text getName() {
        return Text.literal("Packet Logger");
    }
    @Override
    public boolean changeSetting(String id, boolean toggle) {
        NbtCompound setting = (NbtCompound) this.config.get(id);
        assert setting != null;
        setting.putBoolean("toggle", toggle);
        return toggle;
    }
    @Override
    public int changeSetting(String id, int slider) {
        NbtCompound setting = (NbtCompound) this.config.get(id);
        assert setting != null;
        setting.putInt("slider", slider);
        return slider;
    }

    public boolean isEnabled() {
        NbtCompound setting = (NbtCompound) this.config.get("enabled");
        assert setting != null;
        return setting.getBoolean("toggle");
    }
    public boolean shouldLogC2S() {
        NbtCompound setting = (NbtCompound) this.config.get("outgoing");
        assert setting != null;
        return setting.getBoolean("toggle");
    }
    public boolean shouldLogS2C() {
        NbtCompound setting = (NbtCompound) this.config.get("incoming");
        assert setting != null;
        return setting.getBoolean("toggle");
    }
}

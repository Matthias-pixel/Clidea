package de.ideaonic703.clidea.module;

import de.ideaonic703.clidea.ClideaModules;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;

public class LOBotBypassModule implements ClideaModule {
    private static final String ID = "LOBotBypassModule";
    private static LOBotBypassModule instance;
    public static LOBotBypassModule getInstance() {
        if(instance == null)
            instance = new LOBotBypassModule();
        return instance;
    }
    private LOBotBypassModule() {}
    private static final NbtCompound DEFAULT_CONFIG;
    static {
        DEFAULT_CONFIG = new NbtCompound();
        NbtCompound enabled = new NbtCompound();
        enabled.putString("name", "Enabled");
        enabled.putBoolean("toggle", false);
        DEFAULT_CONFIG.put("enabled", enabled);
        NbtCompound block_vehicle_packets = new NbtCompound();
        block_vehicle_packets.putString("name", "Disable Vehicles");
        block_vehicle_packets.putBoolean("toggle", false);
        DEFAULT_CONFIG.put("block_vehicle_packets", block_vehicle_packets);
        NbtCompound round_vehicle_packets = new NbtCompound();
        round_vehicle_packets.putString("name", "Apply Bypass to vehicles");
        round_vehicle_packets.putBoolean("toggle", false);
        DEFAULT_CONFIG.put("round_vehicle_packets", round_vehicle_packets);
        NbtCompound rounding = new NbtCompound();
        rounding.putString("name", "Rounding precision");
        rounding.putInt("slider_start", 50);
        rounding.putInt("slider", 100);
        rounding.putInt("slider_end", 150);
        DEFAULT_CONFIG.put("rounding", rounding);
    }
    private NbtCompound config = DEFAULT_CONFIG;
    @Override
    public String getId() {
        return ID;
    }
    @Override
    public void setConfig(NbtCompound config) {
        this.config = config;
    }
    @Override
    public NbtCompound getConfig() {
        return this.config;
    }

    @Override
    public Text getName() {
        return Text.literal("LO Bot Bypass");
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
    public boolean isVehicleBlocked() {
        NbtCompound setting = (NbtCompound) this.config.get("block_vehicle_packets");
        assert setting != null;
        return setting.getBoolean("toggle");
    }
    public boolean isVehicleRounded() {
        NbtCompound setting = (NbtCompound) this.config.get("round_vehicle_packets");
        assert setting != null;
        return setting.getBoolean("toggle");
    }
    public double getRounding() {
        NbtCompound setting = (NbtCompound) this.config.get("rounding");
        assert setting != null;
        return setting.getInt("slider");
    }
    public double round(double a) {
        return ((int) (a * this.getRounding())) / this.getRounding();
    }
}

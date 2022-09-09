package de.ideaonic703.clidea;

import de.ideaonic703.clidea.module.LOBotBypassModule;
import de.ideaonic703.clidea.module.PacketLoggerModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ClideaClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("clidea");
	public static final Path CONFIG_DIRECTORY = FabricLoader.getInstance().getConfigDir().resolve("Clidea");
	public static final File CONFIG_FILE = CONFIG_DIRECTORY.resolve("clidea.conf").toFile();
	@Override
	public void onInitializeClient() {
		try {
			if(CONFIG_DIRECTORY.toFile().isDirectory() || CONFIG_DIRECTORY.toFile().mkdirs()) {
				if (CONFIG_FILE.createNewFile()) {
					NbtIo.write(new NbtCompound(), CONFIG_FILE);
				}
			} else {
				throw new RuntimeException("Could not create config file!");
			}
		} catch (IOException ignored) {}
		ClideaModules.getInstance().registerModule(LOBotBypassModule.getInstance());
		ClideaModules.getInstance().registerModule(PacketLoggerModule.getInstance());
	}
}

package de.ideaonic703.clidea;

import com.mojang.logging.LogUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.slf4j.Logger;

public class PositionLogger {
    public static final Logger LOGGER = LogUtils.getLogger();
    public final static double ROUNDING = 100d;
    public final static boolean DISABLE_VEHICLES = true;
    public static double round(double a) {
        return ((int) (a * PositionLogger.ROUNDING)) / PositionLogger.ROUNDING;
    }

    public static void logPacket(PlayerMoveC2SPacket packet) {
        LOGGER.info(String.format("X: %s, Y: %s, Z: %s", packet.getX(0f), packet.getY(0f), packet.getZ(0f)));
    }
}

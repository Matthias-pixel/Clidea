package de.ideaonic703.clidea.mixin;

import de.ideaonic703.clidea.PositionLogger;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerMoveC2SPacket.class)
public class LoggingMixin {
    @Inject(method = "apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V", at = @At("HEAD"))
    public void apply(ServerPlayPacketListener serverPlayPacketListener, CallbackInfo ci) {
        PositionLogger.logPacket((PlayerMoveC2SPacket)(Object)this);
    }
}

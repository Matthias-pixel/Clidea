package de.ideaonic703.clidea.mixin;

import de.ideaonic703.clidea.PositionLogger;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ConnectionMixin {
    @ModifyVariable(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at=@At("HEAD"), ordinal = 0)
    private Packet<?> injectSend(Packet<?> packet) {
        if(packet instanceof PlayerMoveC2SPacket packetorig) {
            if(!packetorig.changesPosition())
                    return packet;
            if(packetorig.changesLook()) {
                packet = new PlayerMoveC2SPacket.Full(
                        PositionLogger.round(packetorig.getX(0f)),
                        packetorig.getY(0f),
                        PositionLogger.round(packetorig.getZ(0f)),
                        packetorig.getYaw(0f), packetorig.getPitch(0f), packetorig.isOnGround());
            } else {
                packet = new PlayerMoveC2SPacket.PositionAndOnGround(
                        PositionLogger.round(packetorig.getX(0f)),
                        packetorig.getY(0f),
                        PositionLogger.round(packetorig.getZ(0f)),
                        packetorig.isOnGround());
            }
        }
        return packet;
    }
    @Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at=@At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if(PositionLogger.DISABLE_VEHICLES && packet instanceof VehicleMoveC2SPacket)
            ci.cancel();
    }
}

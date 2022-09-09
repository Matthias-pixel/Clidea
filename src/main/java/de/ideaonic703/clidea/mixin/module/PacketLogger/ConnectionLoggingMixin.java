package de.ideaonic703.clidea.mixin.module.PacketLogger;

import de.ideaonic703.clidea.module.PacketLoggerModule;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ConnectionLoggingMixin {
    @Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at=@At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if(PacketLoggerModule.getInstance().isEnabled()) {
            if (PacketLoggerModule.getInstance().outgoingPacket(packet, callbacks)) {
                ci.cancel();
            }
        }
    }
}

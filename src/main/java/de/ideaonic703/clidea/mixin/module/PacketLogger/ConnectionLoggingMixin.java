package de.ideaonic703.clidea.mixin.module.PacketLogger;

import de.ideaonic703.clidea.module.PacketLoggerModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ConnectionLoggingMixin {
    @Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at=@At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if(PacketLoggerModule.getInstance().isEnabled() && PacketLoggerModule.getInstance().shouldLogC2S() && MinecraftClient.getInstance().world != null) {
            if (PacketLoggerModule.getInstance().outgoingPacket(packet, callbacks)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "handlePacket", at=@At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(PacketLoggerModule.getInstance().isEnabled() && PacketLoggerModule.getInstance().shouldLogS2C() && MinecraftClient.getInstance().world != null) {
            if (PacketLoggerModule.getInstance().incomingPacket(packet, listener)) {
                ci.cancel();
            }
        }
    }
}

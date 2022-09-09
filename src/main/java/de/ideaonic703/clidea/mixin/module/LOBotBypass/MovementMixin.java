package de.ideaonic703.clidea.mixin.module.LOBotBypass;

import de.ideaonic703.clidea.module.LOBotBypassModule;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VehicleMoveC2SPacket.class)
public class MovementMixin {
	@Inject(method = "write", at = @At("HEAD"), cancellable = true)
	private void injected(PacketByteBuf buf, CallbackInfo ci) {
		if(!LOBotBypassModule.getInstance().isVehicleRounded())
			return;
		VehicleMoveC2SPacket thiz = (VehicleMoveC2SPacket)(Object)this;
		buf.writeDouble(LOBotBypassModule.getInstance().round(thiz.getX()));
		buf.writeDouble(thiz.getY());
		buf.writeDouble(LOBotBypassModule.getInstance().round(thiz.getZ()));
		buf.writeFloat(thiz.getYaw());
		buf.writeFloat(thiz.getPitch());
		ci.cancel();
	}
}

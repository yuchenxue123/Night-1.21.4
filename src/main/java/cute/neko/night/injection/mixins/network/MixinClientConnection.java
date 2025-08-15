package cute.neko.night.injection.mixins.network;

import cute.neko.night.event.EventManager;
import cute.neko.night.event.PacketType;
import cute.neko.night.event.events.game.network.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/01/15 - 13:59
 */

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        final PacketEvent event = new PacketEvent(packet, PacketType.SEND);
        EventManager.INSTANCE.callEvent(event);
        if (event.getCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handlePacket", at = @At(value = "HEAD"), cancellable = true)
    private static void onReceivePacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        final PacketEvent event = new PacketEvent(packet, PacketType.RECEIVE);
        EventManager.INSTANCE.callEvent(event);
        if (event.getCancelled()) {
            ci.cancel();
        }
    }
}

package cute.neko.night.injection.mixins.client;

import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.client.GameShutdownEvent;
import cute.neko.night.event.events.game.client.GameInitializeEvent;
import cute.neko.night.event.events.game.client.GameTickEvent;
import cute.neko.night.event.events.game.misc.WorldEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void startClient(CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new GameInitializeEvent());
    }

    @Inject(method = "stop", at = @At(value = "HEAD"))
    private void stopClient(CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new GameShutdownEvent());
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void hookTickEvent(CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new GameTickEvent());
    }

    @Inject(method = "setWorld", at = @At(value = "HEAD"))
    private void hookWorldSwitchEvent(ClientWorld world, CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new WorldEvent(world));
    }
}

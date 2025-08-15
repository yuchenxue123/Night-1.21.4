package cute.neko.night.injection.mixins.client;

import cute.neko.event.EventManager;
import cute.neko.night.Night;
import cute.neko.night.event.events.game.client.GameShutdownEvent;
import cute.neko.night.event.events.game.client.GameStartEvent;
import cute.neko.night.event.events.game.client.GameTickEvent;
import cute.neko.night.event.events.game.misc.WorldEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/01/12 - 19:05
 */

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void startClient(CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new GameStartEvent());
        Night.INSTANCE.initiate();
    }

    @Inject(method = "stop", at = @At(value = "HEAD"))
    private void stopClient(CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new GameShutdownEvent());
        Night.INSTANCE.shutdown();
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

package cute.neko.injection.mixins.client;

import cute.neko.event.EventManager;
import cute.neko.night.Night;
import cute.neko.night.event.events.game.misc.ClientShutdownEvent;
import cute.neko.night.event.events.game.misc.ClientStartEvent;
import cute.neko.night.event.events.game.misc.SwitchWorldEvent;
import cute.neko.night.utils.render.utils.FrameBufferRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
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

    @Shadow
    public abstract Window getWindow();

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void startClient(CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new ClientStartEvent());
        Night.INSTANCE.initiate();
    }

    @Inject(method = "stop", at = @At(value = "HEAD"))
    private void stopClient(CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new ClientShutdownEvent());
        Night.INSTANCE.shutdown();
    }

    @Inject(method = "setWorld", at = @At(value = "HEAD"))
    private void setWorld(ClientWorld world, CallbackInfo info) {
        EventManager.INSTANCE.callEvent(new SwitchWorldEvent(world));
    }
}

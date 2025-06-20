package cute.neko.injection.mixins.render;

import com.llamalad7.mixinextras.sugar.Local;
import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.render.ScreenRenderEvent;
import cute.neko.night.event.events.game.render.WorldRenderEvent;
import cute.neko.night.module.render.ModuleNoHurtCam;
import cute.neko.night.utils.nano.NanoUtils;
import kotlin.Unit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/05/03
 */

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow @Final public BufferBuilderStorage buffers;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V"))
    public void hookWorldRenderEvent(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f2) {
        var newMatStack = new MatrixStack();

        newMatStack.multiplyPositionMatrix(matrix4f2);

        EventManager.INSTANCE.callEvent(new WorldRenderEvent(newMatStack, this.camera, tickCounter.getTickDelta(false)));
    }

    @Inject(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/util/Pool;decrementLifespan()V",
            shift = At.Shift.AFTER)
    )
    public void hookScreenRenderEvent(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local Matrix4fStack stack) {
        if (client.isFinishedLoading() && client.world != null
                && client.getOverlay() == null
                && !(client.currentScreen instanceof LevelLoadingScreen)
        ) {
            DrawContext context = new DrawContext(client, this.buffers.getEntityVertexConsumers());

            NanoUtils.INSTANCE.draw(() -> {
                EventManager.INSTANCE.callEvent(new ScreenRenderEvent(context, client.getWindow()));
                return Unit.INSTANCE;
            });
        }
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void noHurtCamHook(MatrixStack matrixStack, float f, CallbackInfo ci) {
        if (ModuleNoHurtCam.INSTANCE.getRunning()) {
            ci.cancel();
        }
    }
}

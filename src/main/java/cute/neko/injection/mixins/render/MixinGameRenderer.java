package cute.neko.injection.mixins.render;

import com.llamalad7.mixinextras.sugar.Local;
import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.render.WorldRenderEvent;
import cute.neko.night.features.module.render.ModuleNoHurtCam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
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

    @Shadow
    @Final
    public BufferBuilderStorage buffers;


    @Inject(method = "renderWorld",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    public void hookWorldRenderEvent(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f2) {
        var newMatStack = new MatrixStack();

        newMatStack.multiplyPositionMatrix(matrix4f2);

        EventManager.INSTANCE.callEvent(new WorldRenderEvent(newMatStack, this.camera, tickCounter.getTickDelta(false)));
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void noHurtCamHook(MatrixStack matrixStack, float f, CallbackInfo ci) {
        if (ModuleNoHurtCam.INSTANCE.getRunning()) {
            ci.cancel();
        }
    }
}

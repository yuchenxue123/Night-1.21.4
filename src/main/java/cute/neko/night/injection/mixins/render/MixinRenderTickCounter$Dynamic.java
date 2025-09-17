package cute.neko.night.injection.mixins.render;

import cute.neko.night.utils.client.Timer;
import net.minecraft.client.render.RenderTickCounter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTickCounter.Dynamic.class)
public class MixinRenderTickCounter$Dynamic {
    @Shadow
    private float dynamicDeltaTicks;

    @Inject(
            method = "beginRenderTick(J)I",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastTimeMillis:J",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            )
    )
    public void onBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> ci) {
        float timerSpeed = Timer.INSTANCE.get();

        if (timerSpeed > 0) {
            dynamicDeltaTicks *= timerSpeed;
        }
    }
}

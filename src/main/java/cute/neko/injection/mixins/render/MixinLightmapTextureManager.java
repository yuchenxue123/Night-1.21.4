package cute.neko.injection.mixins.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cute.neko.night.module.render.ModuleBrightness;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager {

    @ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"))
    private Object modifyExpressionValue(Object original) {

        if (ModuleBrightness.BrightnessGamma.INSTANCE.getRunning()) {
            return ModuleBrightness.BrightnessGamma.INSTANCE.getGamma();
        }

        return original;
    }
}

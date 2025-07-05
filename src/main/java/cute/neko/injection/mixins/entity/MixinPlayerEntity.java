package cute.neko.injection.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cute.neko.night.utils.rotation.RotationManager;
import cute.neko.night.utils.rotation.features.MovementCorrection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends MixinLivingEntity {

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getYaw()F"))
    private float hookFixRotation(float original) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return original;
        }

        var rotationManager = RotationManager.INSTANCE;
        var rotation = rotationManager.getCurrentRotation();
        var activeRequest = rotationManager.getActiveRequest();

        if (activeRequest == null || activeRequest.getCorrection() == MovementCorrection.NONE || rotation == null) {
            return original;
        }

        return rotation.getYaw();
    }

}

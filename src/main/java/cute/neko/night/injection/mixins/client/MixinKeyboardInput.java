package cute.neko.night.injection.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cute.neko.event.EventManager;
import cute.neko.night.event.events.game.misc.MovementInputEvent;
import cute.neko.night.utils.movement.DirectionalInput;
import cute.neko.night.utils.rotation.RotationManager;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

@Mixin(KeyboardInput.class)
public class MixinKeyboardInput extends MixinInput {

    @ModifyExpressionValue(method = "tick", at = @At(value = "NEW", target = "(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;"))
    private PlayerInput modifyInput(PlayerInput original) {
        var event = new MovementInputEvent(new DirectionalInput(original), original.jump(), original.sneak());
        EventManager.INSTANCE.callEvent(event);
        var untransformedDirectionalInput = event.getDirectionalInput();

        this.untransformed = new PlayerInput(
                untransformedDirectionalInput.getForwards(),
                untransformedDirectionalInput.getBackwards(),
                untransformedDirectionalInput.getLeft(),
                untransformedDirectionalInput.getRight(),
                event.getJump(),
                event.getSneak(),
                original.sprint()
        );

        DirectionalInput transformed = RotationManager.INSTANCE.transformInput(untransformedDirectionalInput);

        return new PlayerInput(
                transformed.getForwards(),
                transformed.getBackwards(),
                transformed.getLeft(),
                transformed.getRight(),
                event.getJump(),
                event.getSneak(),
                original.sprint()
        );
    }
}

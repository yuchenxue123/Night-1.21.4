package cute.neko.night.injection.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import cute.neko.night.features.module.movement.ModuleSprint;
import cute.neko.night.injection.addition.InputAddition;
import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Input.class)
public class MixinInput implements InputAddition {
    @Shadow
    public float movementForward;

    @Shadow
    public float movementSideways;

    @Unique
    protected PlayerInput untransformed = PlayerInput.DEFAULT;

    @ModifyReturnValue(method = "hasForwardMovement", at = @At("RETURN"))
    private boolean hookOmnidirectionalSprint(boolean original) {
        // Allow omnidirectional sprinting
        if (ModuleSprint.INSTANCE.isForced()) {
            return Math.abs(this.movementForward) > 1.0E-5F || Math.abs(this.movementSideways) > 1.0E-5F;
        }

        return original;
    }

    @Override
    public PlayerInput neko$getUntransformed() {
        return untransformed;
    }
}

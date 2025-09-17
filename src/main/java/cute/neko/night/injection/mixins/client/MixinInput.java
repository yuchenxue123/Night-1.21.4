package cute.neko.night.injection.mixins.client;

import cute.neko.night.injection.addition.InputAddition;
import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Input.class)
public class MixinInput implements InputAddition {
    @Shadow
    protected Vec2f movementVector;
    @Unique
    protected PlayerInput untransformed = PlayerInput.DEFAULT;

    @Override
    public PlayerInput neko$getUntransformed() {
        return untransformed;
    }

    @Override
    public void neko$setMovementVector(Vec2f vec2f) {
        this.movementVector = vec2f;
    }
}

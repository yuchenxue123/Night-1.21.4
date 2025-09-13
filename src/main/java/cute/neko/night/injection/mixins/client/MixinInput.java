package cute.neko.night.injection.mixins.client;

import cute.neko.night.injection.addition.InputAddition;
import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Input.class)
public class MixinInput implements InputAddition {
    @Unique
    protected PlayerInput untransformed = PlayerInput.DEFAULT;

    @Override
    public PlayerInput neko$getUntransformed() {
        return untransformed;
    }
}

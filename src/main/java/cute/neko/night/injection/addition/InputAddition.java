package cute.neko.night.injection.addition;

import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;

public interface InputAddition {
    PlayerInput neko$getUntransformed();

    void neko$setMovementVector(Vec2f vec2f);
}

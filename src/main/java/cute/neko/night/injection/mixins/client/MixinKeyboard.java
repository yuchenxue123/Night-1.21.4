package cute.neko.night.injection.mixins.client;

import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.misc.KeyboardEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/01/12 - 19:51
 */

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;",
            shift = At.Shift.BEFORE,
            ordinal = 0))
    private void hookKeyboardKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        var inputKey = InputUtil.fromKeyCode(key, scancode);

        EventManager.INSTANCE.callEvent(new KeyboardEvent(inputKey, key, scancode, action));
    }
}

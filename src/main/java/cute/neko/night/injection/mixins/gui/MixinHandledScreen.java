package cute.neko.night.injection.mixins.gui;

import cute.neko.night.event.EventManager;
import cute.neko.night.mod.mousetweaks.HandledScreenKeyboardInputEvent;
import cute.neko.night.mod.mousetweaks.HandledScreenMouseInputEvent;
import cute.neko.night.mod.mousetweaks.HandledScreenRenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class MixinHandledScreen extends Screen {

    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void hookScreenRenderEvent(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        EventManager.INSTANCE.callEvent(new HandledScreenRenderEvent(mouseX, mouseY));
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;matchesMouse(I)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void hookMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        EventManager.INSTANCE.callEvent(new HandledScreenMouseInputEvent(button, HandledScreenMouseInputEvent.Type.CLICKED));
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void hookMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        EventManager.INSTANCE.callEvent(new HandledScreenMouseInputEvent(button, HandledScreenMouseInputEvent.Type.RELEASED));
    }

    @Inject(method = "mouseDragged", at = @At(value = "HEAD"))
    private void hookMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        EventManager.INSTANCE.callEvent(new HandledScreenMouseInputEvent(button, HandledScreenMouseInputEvent.Type.DRAGGED));
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;handleHotbarKeyPressed(II)Z", shift = At.Shift.AFTER))
    private void hookKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        EventManager.INSTANCE.callEvent(new HandledScreenKeyboardInputEvent(keyCode, scanCode));
    }
}

package cute.neko.night.injection.mixins.gui;

import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.misc.ChatSendEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Inject(method = "sendMessage", at = @At(value = "HEAD"), cancellable = true)
    private void handleSendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
        ChatSendEvent event = new ChatSendEvent(chatText);
        EventManager.INSTANCE.callEvent(event);
        if (event.getCancelled()) {
            ci.cancel();
        }
    }
}

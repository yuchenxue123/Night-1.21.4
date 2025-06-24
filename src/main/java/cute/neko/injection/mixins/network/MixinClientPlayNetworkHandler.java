package cute.neko.injection.mixins.network;

import cute.neko.night.features.command.CommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/06/02
 */

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Inject(
            method = "sendChatMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/message/MessageChain$Packer;pack(Lnet/minecraft/network/message/MessageBody;)Lnet/minecraft/network/message/MessageSignatureData;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void hookClientCommandMessage(String content, CallbackInfo ci) {
        if (CommandManager.INSTANCE.predictExecute(content)) {
            CommandManager.INSTANCE.receiveInput(content);
            ci.cancel();
        }
    }
}

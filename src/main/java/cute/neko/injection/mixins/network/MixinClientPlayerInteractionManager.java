package cute.neko.injection.mixins.network;

import cute.neko.event.EventManager;
import cute.neko.night.event.events.game.player.PlayerAttackEntityEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/07/09
 */

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "attackEntity", at = @At(value = "HEAD"), cancellable = true)
    private void hookAttackEntityEvent(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (player != client.player) {
            return;
        }

        final PlayerAttackEntityEvent playerAttackEntityEvent = new PlayerAttackEntityEvent(target);
        EventManager.INSTANCE.callEvent(playerAttackEntityEvent);

        if (playerAttackEntityEvent.getCancelled()) {
            ci.cancel();
        }
    }
}

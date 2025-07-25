package cute.neko.injection.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cute.neko.event.EventManager;
import cute.neko.event.LifecycleEventState;
import cute.neko.injection.addition.ClientPlayerEntityAddition;
import cute.neko.night.event.events.game.player.PlayerMotionEvent;
import cute.neko.night.event.events.game.player.PlayerMovementTickEvent;
import cute.neko.night.event.events.game.player.PlayerTickEvent;
import cute.neko.night.event.events.game.player.PlayerUseMultiplier;
import cute.neko.night.utils.rotation.RotationManager;
import cute.neko.night.utils.rotation.data.Rotation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/01/12 - 20:26
 */

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends MixinPlayerEntity implements ClientPlayerEntityAddition {

    @Shadow
    @Final
    protected MinecraftClient client;

    @Shadow
    public Input input;

    @Unique
    private PlayerMotionEvent playerMotionEvent;

    @Unique
    private int groundTicks = 0;
    @Unique
    private int airTicks = 0;

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ),
            cancellable = true
    )
    private void hookTickEvent(CallbackInfo ci) {
        if ((Object) this != client.player) {
            return;
        }

        var tickEvent = new PlayerTickEvent();
        EventManager.INSTANCE.callEvent(tickEvent);

        if (tickEvent.getCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    private void hookMovementTickEvent(CallbackInfo callbackInfo) {
        if ((Object) this != client.player) {
            return;
        }

        EventManager.INSTANCE.callEvent(new PlayerMovementTickEvent());
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void hookMovementPre(CallbackInfo callbackInfo) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        if ((Object) this != client.player) {
            return;
        }

        playerMotionEvent = new PlayerMotionEvent(
                player.getX(), player.getY(), player.getZ(),
                player.getYaw(), player.getPitch(),
                player.isOnGround(),
                LifecycleEventState.PRE
        );
        EventManager.INSTANCE.callEvent(playerMotionEvent);

        if (playerMotionEvent.getCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0))
    private void hookCustomMultiplier(CallbackInfo callbackInfo) {
        if ((Object) this != client.player) {
            return;
        }

        final Input input = this.input;
        // reverse
        input.movementForward /= 0.2f;
        input.movementSideways /= 0.2f;

        // then
        final PlayerUseMultiplier playerUseMultiplier = new PlayerUseMultiplier(0.2f, 0.2f);
        EventManager.INSTANCE.callEvent(playerUseMultiplier);
        input.movementForward *= playerUseMultiplier.getForward();
        input.movementSideways *= playerUseMultiplier.getSideways();
    }

    @ModifyExpressionValue(
            method = "sendMovementPackets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getX()D"
            )
    )
    private double modifyXPosition(double original) {
        if ((Object) this != client.player) {
            return original;
        }

        return playerMotionEvent.getX();
    }

    @ModifyExpressionValue(
            method = "sendMovementPackets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getY()D"
            )
    )
    private double modifyYPosition(double original) {
        if ((Object) this != client.player) {
            return original;
        }

        return playerMotionEvent.getY();
    }

    @ModifyExpressionValue(
            method = "sendMovementPackets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getZ()D"
            )
    )
    private double modifyZPosition(double original) {
        if ((Object) this != client.player) {
            return original;
        }

        return playerMotionEvent.getZ();
    }

    @ModifyExpressionValue(
            method = "sendMovementPackets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isOnGround()Z"
            )
    )
    private boolean modifyOnGround(boolean original) {
        if ((Object) this != client.player) {
            return original;
        }

        return playerMotionEvent.getOnGround();
    }

    @ModifyExpressionValue(
            method = {"sendMovementPackets", "tick"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"
            )
    )
    private float hookSilentRotationYaw(float original) {
        if ((Object) this != client.player) {
            return original;
        }

        Rotation rotation = RotationManager.INSTANCE.getCurrentRotation();
        if (rotation == null) {
            return original;
        }

        return rotation.getYaw();
    }

    @ModifyExpressionValue(
            method = {"sendMovementPackets", "tick"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"
            )
    )
    private float hookSilentRotationPitch(float original) {
        if ((Object) this != client.player) {
            return original;
        }

        Rotation rotation = RotationManager.INSTANCE.getCurrentRotation();
        if (rotation == null) {
            return original;
        }

        return rotation.getPitch();
    }

    @Inject(method = "sendMovementPackets", at = @At("TAIL"))
    private void hookMovementPost(CallbackInfo callbackInfo) {

        if ((Object) this != client.player) {
            return;
        }

        EventManager.INSTANCE.callEvent(
                new PlayerMotionEvent(
                        this.getX(), this.getY(), this.getZ(),
                        this.getYaw(), this.getPitch(),
                        this.isOnGround(),
                        LifecycleEventState.POST
                )
        );
    }

    @Inject(method = "move", at = @At("RETURN"))
    private void hookGroundAirTimeCounters(CallbackInfo ci) {
        if (this.isOnGround()) {
            groundTicks++;
            airTicks = 0;
        } else {
            airTicks++;
            groundTicks = 0;
        }
    }

    @Override
    public int neko$getGroundTicks() {
        return groundTicks;
    }

    @Override
    public int neko$getAirTicks() {
        return airTicks;
    }
}

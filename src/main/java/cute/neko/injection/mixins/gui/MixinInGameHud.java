package cute.neko.injection.mixins.gui;

import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.render.ScreenRenderEvent;
import cute.neko.night.features.module.render.ModuleInterface;
import cute.neko.night.utils.nano.NanoUtils;
import kotlin.Unit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/06/17
 */

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderMiscOverlays", at = @At(value = "TAIL"))
    private void hookScreenEvent(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        NanoUtils.INSTANCE.draw(() -> {
            EventManager.INSTANCE.callEvent(new ScreenRenderEvent(context, client.getWindow(), tickCounter.getTickDelta(false)));
            return Unit.INSTANCE;
        });
    }

    @Redirect(
            method = "renderMiscOverlays",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignetteOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/Entity;)V"
            )
    )
    private void hookRemoveVignette(InGameHud instance, DrawContext context, Entity entity) {
        if (!ModuleInterface.INSTANCE.getShouldHideVignette()) {
            instance.renderVignetteOverlay(context, entity);
        }
    }
}

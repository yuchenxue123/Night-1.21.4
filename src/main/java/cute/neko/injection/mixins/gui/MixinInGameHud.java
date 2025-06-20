package cute.neko.injection.mixins.gui;

import cute.neko.night.module.render.ModuleInterface;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author yuchenxue
 * @date 2025/06/17
 */

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Redirect(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignetteOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/Entity;)V"))
    private void hookRemoveVignette(InGameHud instance, DrawContext context, Entity entity) {
        if (!ModuleInterface.INSTANCE.getShouldHideVignette()) {
            instance.renderVignetteOverlay(context, entity);
        }
    }
}

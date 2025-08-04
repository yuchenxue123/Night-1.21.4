package cute.neko.injection.mixins.render;

import cute.neko.night.features.module.render.ModuleNameTags;
import cute.neko.night.features.module.render.share.SharedTargetOption;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author yuchenxue
 * @date 2025/06/24
 */

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity, S extends EntityRenderState> {

    @Shadow
    protected abstract void renderLabelIfPresent(S state, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void hookRenderLabel(EntityRenderer<T, S> instance, S state, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (ModuleNameTags.INSTANCE.getRunning() && (this.dispatcher.targetedEntity == null || SharedTargetOption.INSTANCE.isTarget(this.dispatcher.targetedEntity))) {
            return;
        }

        this.renderLabelIfPresent(state, state.displayName, matrices, vertexConsumers, light);
    }
}

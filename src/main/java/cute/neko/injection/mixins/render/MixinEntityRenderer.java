package cute.neko.injection.mixins.render;

import cute.neko.night.features.module.render.ModuleNameTags;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author yuchenxue
 * @date 2025/06/24
 */

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity, S extends EntityRenderState> {

    @ModifyVariable(
            method = "renderLabelIfPresent",
            at = @At(
                    value = "STORE",
                    opcode = Opcodes.ISTORE
            ),
            ordinal = 0
    )
    private boolean modifyShowName(boolean original) {
        return ModuleNameTags.NameTagsNormal.INSTANCE.getRunning();
    }
}

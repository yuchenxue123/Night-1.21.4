package cute.neko.injection.mixins.entity;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends MixinLivingEntity {
}

package cute.neko.night.setting.config.types

import cute.neko.night.setting.config.Configurable
import cute.neko.night.setting.type.mode.SubMode
import cute.neko.night.utils.client.mc
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

class TargetOption : Configurable("Target") {

    private val targets by enum("Targets", TargetType.PLAYERS)

    fun isTarget(entity: LivingEntity): Boolean {
        return when (entity) {
            mc.player -> false
            is MobEntity -> false
            is PlayerEntity -> entity.isAlive && !entity.isInvisible
            else -> true
        }
    }

    enum class TargetType(override val modeName: String) : SubMode {
        PLAYERS("Players"),
        MOBS("Mobs")
    }
}
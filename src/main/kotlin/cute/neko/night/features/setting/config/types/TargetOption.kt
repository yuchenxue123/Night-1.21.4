package cute.neko.night.features.setting.config.types

import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.type.mode.SubMode
import cute.neko.night.utils.client.mc
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

open class TargetOption : Configurable("Target") {

    private val targets = enum("Targets", TargetType.PLAYERS)

    fun isTarget(entity: LivingEntity): Boolean {
        if (entity == mc.player) return false

        return TargetType.entries.any { targets.isActive(it) && it.isTarget(entity) }
    }

    enum class TargetType(override val modeName: String) : SubMode {
        PLAYERS("Players") {
            override fun isTarget(entity: LivingEntity): Boolean {
                return entity is PlayerEntity && entity.isAlive && !entity.isInvisible
            }
        },
        INVISIBLE("Invisible") {
            override fun isTarget(entity: LivingEntity): Boolean {
                return entity is PlayerEntity && entity.isAlive && entity.isInvisible
            }
        },
        FRIENDLY("Friendly") {
            override fun isTarget(entity: LivingEntity): Boolean {
                return entity is PassiveEntity
            }
        },
        HOSTILE("Hostile") {
            override fun isTarget(entity: LivingEntity): Boolean {
                return entity is HostileEntity || entity is SlimeEntity
            }
        },

        IRONMAN("IronMan") {
            override fun isTarget(entity: LivingEntity): Boolean {
                return entity is IronGolemEntity
            }
        }

        ;

        abstract fun isTarget(entity: LivingEntity): Boolean
    }
}
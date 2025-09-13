package cute.neko.night.features.module.combat.killaura.features

import cute.neko.night.features.module.combat.killaura.ModuleKillAura
import cute.neko.night.features.module.combat.killaura.ModuleKillAura.range
import cute.neko.night.features.module.combat.killaura.ModuleKillAura.wallRange
import cute.neko.night.features.setting.config.types.EmptyConfigurable
import cute.neko.night.features.setting.type.mode.SubMode
import cute.neko.night.utils.entity.rotation
import cute.neko.night.utils.misc.RandomUtils
import cute.neko.night.utils.rotation.RaytraceUtils
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.time.TimeTracker
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Hand

object KillAuraAttackExecutor : EmptyConfigurable("AttackExecutor", ModuleKillAura) {
    private val clickMode by mode("ClickMode", ClickMode.STABILIZED)

    // cps
    private val tracker = TimeTracker()

    enum class ClickMode(override val modeName: String) : SubMode {
        COOLDOWN("Cooldown") {
            override fun execute(): Boolean {
                return player.getAttackCooldownProgress(0f) >= 1f
            }
        },
        STABILIZED("Stabilized") {
            override fun execute(): Boolean {
                val delay = 1000L / cps

                if (tracker.hasPassedTime(delay)) {
                    tracker.reset()
                    return true
                }

                return false
            }
        },
        RANDOM("Random") {
            override fun execute(): Boolean {
                val random = RandomUtils.int(cps, random).coerceIn(1, 20)
                val delay = 1000L / random

                if (tracker.hasPassedTime(delay)) {
                    tracker.reset()
                    return true
                }

                return false
            }
        }
        ;

        abstract fun execute(): Boolean
    }

    private val cps by int("CPS", 12, 1..20) {
        clickMode != ClickMode.COOLDOWN
    }

    private val random by int("Random", 3, 1..5) {
        clickMode == ClickMode.RANDOM
    }

    private val swingMode by mode("SwingMode", SwingMode.POST)

    enum class SwingMode(override val modeName: String) : SubMode {
        PRE("Pre"),
        POST("Post"),
        NONE("None")
    }

    fun attack(entity: LivingEntity, raytrace: Boolean = false) {
        val target = when {
            raytrace -> {
                val result = RaytraceUtils.raytraceEntity(
                    range.toDouble(),
                ) { it is LivingEntity && KillAuraTargetTracker.isTarget(it) }

                result?.entity ?: entity
            }

            else -> entity
        }

        if (!RaytraceUtils.faceEntity(
                toEntity = target,
                rotation = RotationManager.currentRotation ?: player.rotation,
                range = range.toDouble(),
                wallsRange = wallRange.toDouble(),
            )
        ) {
            return
        }

        if (!clickMode.execute()) {
            return
        }

        when (swingMode) {
            SwingMode.PRE -> {
                player.swingHand(Hand.MAIN_HAND)
                interactionManager.attackEntity(player, target)
            }

            SwingMode.POST -> {
                interactionManager.attackEntity(player, target)
                player.swingHand(Hand.MAIN_HAND)
            }

            SwingMode.NONE -> {
                interactionManager.attackEntity(player, target)
            }
        }
    }
}
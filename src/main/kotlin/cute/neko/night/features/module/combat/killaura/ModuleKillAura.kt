package cute.neko.night.features.module.combat.killaura

import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.combat.killaura.features.KillAuraAttackExecutor
import cute.neko.night.features.module.combat.killaura.features.KillAuraTargetTracker
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.rotation.features.MovementCorrection

/**
 * @author yuchenxue
 * @date 2025/07/06
 */

object ModuleKillAura : ClientModule(
    "KillAura",
    ModuleCategory.COMBAT
) {

    private val attackExecutor = tree(KillAuraAttackExecutor)

    val range by float("Range", 3f, 1f..7f, 0.1f)
    val scanRange by float("ScanExpandRange", 0.5f, 0.1f..2f, 0.1f)
    val wallRange by float("WallRange", 1f, 0f..3f, 0.1f)

    val fov by float("FOV", 180f, 1f..180f, 1f)

    private val horizontalSpeed by float("HorizontalSpeed", 180f, 1f..180f, 1f)
    private val verticalSpeed by float("VerticalSpeed", 180f, 1f..180f, 1f)

    private val correction by mode("MovementCorrection", MovementCorrection.NONE)

    private val raytrace by boolean("Raytrace", false)

    private val targetTracker = tree(KillAuraTargetTracker)

    override fun disable() {
        RotationManager.remove(this)
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        val target = targetTracker.findTarget() ?: run {
            RotationManager.remove(this)
            return@handler
        }

        RotationManager.request(
            this,
            target,
            horizontalSpeed = horizontalSpeed,
            verticalSpeed = verticalSpeed,
            correction = correction,
        )

        attackExecutor.attack(target, raytrace)
    }
}
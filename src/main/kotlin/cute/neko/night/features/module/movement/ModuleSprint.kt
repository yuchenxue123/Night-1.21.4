package cute.neko.night.features.module.movement

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.setting.type.mode.SubMode

object ModuleSprint : ClientModule(
    "Sprint",
    ModuleCategory.MOVEMENT
) {

    private val mode by mode("Mode", SprintMode.FORCE)

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {

        when (mode) {
            SprintMode.LEGIT -> {
                if (mc.options.forwardKey.isPressed && player.input.movementForward > 0.8
                    && (!player.horizontalCollision || player.collidedSoftly)
                ) {
                    player.isSprinting = true
                }
            }

            SprintMode.FORCE -> {
                player.isSprinting = true
            }
        }
    }

    private enum class SprintMode(override val modeName: String) : SubMode {
        LEGIT("Legit"),
        FORCE("Force")
    }

    fun isForced() = mode == SprintMode.FORCE
}
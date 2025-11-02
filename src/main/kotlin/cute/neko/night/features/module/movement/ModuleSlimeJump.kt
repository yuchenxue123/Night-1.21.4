package cute.neko.night.features.module.movement

import cute.neko.night.event.events.game.player.PlayerMoveEvent
import cute.neko.night.event.handler
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.movement.ModuleSlimeJump.Mode.*
import cute.neko.night.features.setting.type.mode.SubMode
import cute.neko.night.utils.extensions.getBlock
import net.minecraft.block.Blocks

// From LiquidBounce discord
object ModuleSlimeJump : ClientModule("SlimeJump", ModuleCategory.MOVEMENT) {

    private val mode by mode("Mode", Mode.MODIFY)

    private val velocity by float("Velocity", 1f, 0.5f..5f, 0.05f)

    private val addition by float("Addition", 0.2f, 0f..2f, 0.02f)

    private val max by float("Max", 1f, 0.5f..5f, 0.05f)
    private val min by float("Min", 0.5f, 0.25f..1.5f, 0.05f)

    @Suppress("unused")
    private val onPlayerMove = handler<PlayerMoveEvent> { event ->
        if (player.isOnGround) {
            val pos = player.blockPos.down()

            val onSlime = pos.getBlock() == Blocks.SLIME_BLOCK

            if (onSlime && event.movement.y > min && event.movement.y < max) {

//                debug.debug("MY:" + event.movement.y)

                when (mode) {
                    MODIFY -> {
                        event.movement.y = velocity.toDouble()
                    }
                    ADD -> {
                        event.movement.y += addition.toDouble()
                    }
                }

            }
        }
    }

    enum class Mode(override val modeName: String) : SubMode {
        MODIFY("Modify"),
        ADD("Add")
    }
}
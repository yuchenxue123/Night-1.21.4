package cute.neko.night.features.module.player.scaffold

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.player.scaffold.features.ScaffoldPlace
import cute.neko.night.features.module.player.scaffold.features.ScaffoldRotation
import cute.neko.night.features.module.player.scaffold.features.ScaffoldSearchBlock
import cute.neko.night.utils.kotlin.Priority
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.rotation.data.Rotation
import cute.neko.night.utils.rotation.features.MovementCorrection

object ModuleScaffold : ClientModule(
    "Scaffold",
    ModuleCategory.PLAYER
) {

    private val horizontalSpeed by float("HorizontalSpeed", 180f, 1f..180f, 1f)
    private val verticalSpeed by float("VerticalSpeed", 180f, 1f..180f, 1f)
    private val correction by mode("MovementCorrection", MovementCorrection.NONE)

    private var rotation: Rotation? = null

    override fun disable() {
        ScaffoldSearchBlock.reset()

        RotationManager.remove(this)
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        ScaffoldSearchBlock.working()

        val searchResult = ScaffoldSearchBlock.result ?: return@handler

        rotation = ScaffoldRotation.calculateRotation(searchResult.pos, searchResult.direction)

        val rot = rotation ?: return@handler

        RotationManager.request(
            this,
            rot,
            Priority.ROTATION_IMPORTANT,
            horizontalSpeed,
            verticalSpeed,
            correction,
        )

        ScaffoldPlace.place(searchResult.pos, searchResult.direction)
    }
}
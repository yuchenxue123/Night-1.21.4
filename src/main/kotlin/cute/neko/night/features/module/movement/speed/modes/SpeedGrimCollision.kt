package cute.neko.night.features.module.movement.speed.modes

import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.utils.entity.box
import cute.neko.night.utils.entity.getMovementDirectionOfInput
import cute.neko.night.utils.entity.moving
import cute.neko.night.utils.entity.rotation
import cute.neko.night.utils.movement.DirectionalInput
import cute.neko.night.utils.rotation.RotationManager
import net.minecraft.entity.LivingEntity
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author yuchenxue
 * @date 2025/08/02
 */

object SpeedGrimCollision : SpeedMode("GrimCollision") {

    private val speed by float("Speed", 0.08f, 0.01f..0.1f, 0.01f)

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {

        if (!player.moving) return@handler

        var collisions = 0

        val box = player.box.expand(1.0)

        world.entities
            .filterIsInstance<LivingEntity>()
            .filter { it != player }
            .forEach { entity ->
                val entityBox = entity.box

                if (box.intersects(entityBox)) {
                    collisions++
                }
            }

        if (collisions == 0) return@handler

        val rotation = RotationManager.currentRotation ?: player.rotation
        val yaw = getMovementDirectionOfInput(facingYaw = rotation.yaw, input = DirectionalInput(player.input))
        val speed = speed * collisions

        player.addVelocity(-sin(yaw) * speed.toDouble(), .0, cos(yaw) * speed.toDouble())
    }
}
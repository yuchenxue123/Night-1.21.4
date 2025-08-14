package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.event.handler
import cute.neko.night.event.events.game.misc.MovementInputEvent
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.utils.extensions.getState
import cute.neko.night.utils.rotation.RaytraceUtils
import cute.neko.night.utils.rotation.data.Rotation
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

/**
 * @author yuchenxue
 * @date 2025/08/14
 */

object AntiVelocityGrim : AntiVelocityMode("Grim") {

    private val skipTicks = int("SkipTicks", 2, 1..10)

    private var hasReceiveDamage = false
    private var shouldRotationAndInteractBlock = false

    override fun disable() {
        hasReceiveDamage = false
    }

    @Suppress("unused")
    private val onPacket = handler<PacketEvent> { event ->
        when (val packet = event.packet) {
            is EntityDamageS2CPacket -> {
                if (packet.entityId == player.id) {
                    hasReceiveDamage = true
                }
            }

            is EntityVelocityUpdateS2CPacket -> {
                if (packet.entityId == player.id) {

                    if (packet.velocityX == 0 && packet.velocityZ == 0 && packet.velocityY < 0) {
                        return@handler
                    }

                    val rotation = Rotation(player.yaw, 90f)
                    val state = RaytraceUtils.raytrace(rotation).blockPos.getState() ?: return@handler

                    if (state.isAir || state.isLiquid || player.usingItem) {
                        return@handler
                    }


                }
            }
        }
    }
}
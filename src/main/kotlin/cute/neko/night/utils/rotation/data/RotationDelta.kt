package cute.neko.night.utils.rotation.data

import net.minecraft.util.math.Vec2f
import kotlin.math.hypot

data class RotationDelta(
    val deltaYaw: Float,
    val deltaPitch: Float,
) {
    fun length() = hypot(deltaYaw, deltaPitch)
    fun toVec2f() = Vec2f(deltaYaw, deltaPitch)
}
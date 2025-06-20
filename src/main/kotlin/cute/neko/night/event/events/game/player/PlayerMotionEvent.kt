package cute.neko.night.event.events.game.player

import cute.neko.night.event.Event

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

sealed class PlayerMotionEvent(
    var x: Double,
    var y: Double,
    var z: Double,
    var yaw: Float,
    var pitch: Float,
    var ground: Boolean
) : Event() {
    class Pre(
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
        ground: Boolean
        ) : PlayerMotionEvent(x, y, z, yaw, pitch, ground)

    class Post(
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
        ground: Boolean
    ) : PlayerMotionEvent(x, y, z, yaw, pitch, ground)
}
package cute.neko.night.utils.rotation.data

import cute.neko.night.event.EventListener
import cute.neko.night.utils.rotation.features.MovementCorrection

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

data class RotationTarget(
    val controller: EventListener,
    val rotation: Rotation,
    val correction: MovementCorrection,
    val horizontalSpeed: Float = 180f,
    val verticalSpeed: Float = 180f,
)
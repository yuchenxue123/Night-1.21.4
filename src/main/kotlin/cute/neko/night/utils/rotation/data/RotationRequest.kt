package cute.neko.night.utils.rotation.data

import cute.neko.night.event.EventListener
import cute.neko.night.utils.rotation.features.MovementCorrection

data class RotationRequest(
    val listener: EventListener,
    val rotation: Rotation,
    val priority: Int = 100,
    val horizontalSpeed: Float = 180f,
    val verticalSpeed: Float = 180f,
    val correction: MovementCorrection = MovementCorrection.NONE,
)
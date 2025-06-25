package cute.neko.night.utils.movement

import cute.neko.night.utils.extensions.untransformed
import net.minecraft.client.input.Input
import net.minecraft.util.PlayerInput

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

class DirectionalInput(
    val forwards: Boolean,
    val backwards: Boolean,
    val left: Boolean,
    val right: Boolean,
) {

    constructor(input: Input) : this(
        input.untransformed
    )

    constructor(input: PlayerInput) : this(
        input.forward,
        input.backward,
        input.left,
        input.right
    )

    constructor(movementForward: Float, movementSideways: Float) : this(
        forwards = movementForward > 0.0,
        backwards = movementForward < 0.0,
        left = movementSideways > 0.0,
        right = movementSideways < 0.0
    )

    companion object {
        val NONE = DirectionalInput(forwards = false, backwards = false, left = false, right = false)
        val FORWARDS = DirectionalInput(forwards = true, backwards = false, left = false, right = false)
        val BACKWARDS = DirectionalInput(forwards = false, backwards = true, left = false, right = false)
        val LEFT = DirectionalInput(forwards = false, backwards = false, left = true, right = false)
        val RIGHT = DirectionalInput(forwards = false, backwards = false, left = false, right = true)

        fun of(input: Input): DirectionalInput {
            return DirectionalInput(input.untransformed)
        }

        fun of(input: PlayerInput): DirectionalInput {
            return DirectionalInput(
                input.forward,
                input.backward,
                input.left,
                input.right
            )
        }

        fun of(movementForward: Float, movementSideways: Float): DirectionalInput {
            return DirectionalInput(
                forwards = movementForward > 0.0,
                backwards = movementForward < 0.0,
                left = movementSideways > 0.0,
                right = movementSideways < 0.0
            )
        }
    }
}
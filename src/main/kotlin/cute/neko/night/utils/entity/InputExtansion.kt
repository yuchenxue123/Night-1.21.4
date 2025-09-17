package cute.neko.night.utils.entity

import cute.neko.night.injection.addition.InputAddition
import net.minecraft.client.input.Input
import net.minecraft.util.math.Vec2f

var Input.movementForward
    get() = this.movementInput.x
    set(value) { this.movementInput.x = value }

var Input.movementSideways
    get() = this.movementInput.y
    set(value) { this.movementInput.y = value }

fun Input.setMovementVector(vec: Vec2f) {
    (this as InputAddition).`neko$setMovementVector`(vec)
}

fun Vec2f.div(valueX: Float, valueY: Float) = Vec2f(this.x / valueX, this.y / valueY)
fun Vec2f.multiply(valueX: Float, valueY: Float) = Vec2f(this.x * valueX, this.y * valueY)
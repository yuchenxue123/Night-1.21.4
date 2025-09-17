package cute.neko.night.utils.entity

import net.minecraft.client.input.Input

val Input.movementForward
    get() = this.movementInput.x

val Input.movementSideways
    get() = this.movementInput.x
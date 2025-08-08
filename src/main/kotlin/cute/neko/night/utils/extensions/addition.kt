package cute.neko.night.utils.extensions

import cute.neko.night.injection.addition.ClientPlayerEntityAddition
import cute.neko.night.injection.addition.InputAddition
import net.minecraft.client.input.Input
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.PlayerInput

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

val Input.untransformed: PlayerInput
    get() = (this as InputAddition).`neko$getUntransformed`()

val ClientPlayerEntity.groundTicks
    get() = (this as ClientPlayerEntityAddition).`neko$getGroundTicks`()

val ClientPlayerEntity.airTicks
    get() = (this as ClientPlayerEntityAddition).`neko$getAirTicks`()
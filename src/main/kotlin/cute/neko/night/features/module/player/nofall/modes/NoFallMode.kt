package cute.neko.night.features.module.player.nofall.modes

import cute.neko.night.features.module.player.nofall.ModuleNoFall
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable
import net.minecraft.util.shape.VoxelShapes

open class NoFallMode(name: String) : Choice(name) {
    override val controller: ChoicesConfigurable<*>
        get() = ModuleNoFall.mode

    protected fun voidCheck(void: Double = -64.0): Boolean {
        if (player.y < void) return true

        val box = player.boundingBox
            .withMinY(void)

        return world.getBlockCollisions(player, box)
            .all { shape -> shape == VoxelShapes.empty() }
    }
}
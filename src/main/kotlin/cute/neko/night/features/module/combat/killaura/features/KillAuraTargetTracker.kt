package cute.neko.night.features.module.combat.killaura.features

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.combat.killaura.ModuleKillAura
import cute.neko.night.features.module.combat.killaura.ModuleKillAura.fov
import cute.neko.night.features.module.combat.killaura.ModuleKillAura.range
import cute.neko.night.features.module.combat.killaura.ModuleKillAura.scanRange
import cute.neko.night.features.module.misc.ModuleAntiBot
import cute.neko.night.features.setting.config.types.EmptyConfigurable
import cute.neko.night.features.setting.config.types.TargetOption
import cute.neko.night.utils.rotation.RotationUtils
import net.minecraft.entity.LivingEntity

object KillAuraTargetTracker : EmptyConfigurable("TargetTracker", ModuleKillAura) {
    private val targets = tree(TargetOption())

    var target: LivingEntity? = null

    override fun disable() {
        target = null
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        target = findTarget()
    }

    fun findTarget(): LivingEntity? {
        val player = mc.player ?: return null
        val world = mc.world ?: return null

        return world.entities.filterIsInstance<LivingEntity>()
            .filter { isTarget(it) }
            .minByOrNull { player.distanceTo(it) }
    }

    fun isTarget(entity: LivingEntity): Boolean {
        val player = mc.player ?: return false

        return targets.isTarget(entity)
                && !ModuleAntiBot.isBot(entity)
                && player.distanceTo(entity) <= range + scanRange
                && RotationUtils.rotationDifference(entity) <= fov
                && entity.isAlive
    }
}
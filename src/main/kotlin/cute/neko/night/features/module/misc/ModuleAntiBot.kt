package cute.neko.night.features.module.misc

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import net.minecraft.entity.LivingEntity
import java.util.*

object ModuleAntiBot : ClientModule(
    "AntiBot",
    ModuleCategory.MISC
) {

    private val bots = HashSet<UUID>()

    fun isBot(entity: LivingEntity): Boolean {
        if (!running) return false

        return entity.uuid in bots
    }

    override fun disable() {
        bots.clear()
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        val world = mc.world ?: return@handler
        val network = mc.networkHandler ?: return@handler

        val players = mutableListOf<UUID>()

        network.playerList.forEach { player ->
            players.add(player.profile.id)
        }

        world.players.forEach { player ->
            val id = player.gameProfile.id ?: return@forEach

            if (id !in players && id !in bots)
            {
                bots.add(id)
            }
            else
            if (id in players && id in bots)
            {
                bots.remove(id)
            }
        }
    }
}
package cute.neko.night.features.module.misc

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.misc.KeyboardEvent
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.misc.option.NullableOption
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/08/12
 */

object ModuleKeepContainer : ClientModule(
    "KeepContainer",
    ModuleCategory.MISC
) {

    private val screen = NullableOption<GenericContainerScreen>(null)
    private val saved = BooleanOption(false)

    override fun disable() {
        screen.safeUse {
            network.sendPacket(CloseHandledScreenC2SPacket(it.screenHandler.syncId))
        }
        screen.set(null)
        saved.set(false)
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        val current = mc.currentScreen

        if (current is GenericContainerScreen) {
//            if (current.title.string.contains("传送")) {
                screen.set(current)
                saved.set(true)
//            }
        }
    }

    @Suppress("unused")
    private val onPacket = handler<PacketEvent> { event ->
        val packet = event.packet

        if (saved.get()) {
            if (packet is CloseHandledScreenC2SPacket) {
                screen.safeUse {
//                if (it.screenHandler.syncId == packet.syncId) {
                    event.cancel()
//                }
                }
            }

            if (packet is CloseScreenS2CPacket) {
                screen.safeUse {
                    if (it.screenHandler.syncId == packet.syncId) {
                        screen.set(null)
                    }
                }
            }
        }
    }

    private val onKey = handler<KeyboardEvent> { event ->
        if (event.keyCode == GLFW.GLFW_KEY_Y) {
            screen.safeUse {
                mc.setScreen(it)
                player.currentScreenHandler = it.screenHandler
            }
        }
    }
}
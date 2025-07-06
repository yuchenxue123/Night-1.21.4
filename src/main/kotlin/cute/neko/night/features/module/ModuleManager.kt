package cute.neko.night.features.module

import cute.neko.night.event.EventListener
import cute.neko.night.event.events.game.misc.KeyboardEvent
import cute.neko.night.event.handle
import cute.neko.night.features.module.combat.ModuleKeepSprint
import cute.neko.night.features.module.combat.ModuleKillAura
import cute.neko.night.features.module.combat.antivelocity.ModuleAntiVelocity
import cute.neko.night.features.module.misc.ModuleAntiBot
import cute.neko.night.features.module.misc.ModuleTest
import cute.neko.night.features.module.misc.disabler.ModuleDisabler
import cute.neko.night.features.module.movement.ModuleNoJumpDelay
import cute.neko.night.features.module.movement.ModuleNoPush
import cute.neko.night.features.module.movement.ModuleSprint
import cute.neko.night.features.module.movement.speed.ModuleSpeed
import cute.neko.night.features.module.player.*
import cute.neko.night.features.module.player.nofall.ModuleNoFall
import cute.neko.night.features.module.render.*
import cute.neko.night.ui.widget.type.DynamicIslandWidget
import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/01/12 - 19:10
 */

object ModuleManager : EventListener, Accessor {

    val modules = mutableListOf<ClientModule>()

    fun load() {
        register(
            // combat
            ModuleAntiVelocity,
            ModuleKillAura,
            ModuleKeepSprint,

            // movement
            ModuleSprint,
            ModuleNoJumpDelay,
            ModuleNoPush,
            ModuleSpeed,

            // player
            ModuleBlockFly,
            ModuleStealer,
            ModuleInvManager,
            ModuleEagle,
            ModuleNoFall,
            ModuleDelayPacket,
            ModuleTimer,

            // render
            ModuleBrightness,
            ModuleClickScreen,
            ModuleSettings,
            ModuleRotations,
            ModuleInterface,
            ModuleNoHurtCam,
            ModuleNameTags,

            // misc
            ModuleDisabler,
            ModuleAntiBot,

            ModuleTest,
        )
    }

    /**
     * Get module by name
     *
     * @param name name of module
     * @return founded module
     */
    fun getModule(name: String) = modules.find { it.name.equals(name, true) }

    /**
     * Register modules
     */
    fun register(vararg array: ClientModule) {
        modules.addAll(array)
    }

    /**
     * Register single module
     */
    fun register(module: ClientModule) {
        modules.add(module)
    }

    @Suppress("unused")
    private val onKeyboard = handle<KeyboardEvent> { event ->
        if (mc.currentScreen is ChatScreen) return@handle

        when (event.action) {
            GLFW.GLFW_PRESS -> {
                val selects = modules.filter {
                    it.key == event.keyCode && event.key.category == InputUtil.Type.KEYSYM
                }

                selects.forEach { module ->
                    module.toggle()
                }

                if (selects.isNotEmpty()) {
                    DynamicIslandWidget.push(selects)
                }
            }
        }
    }
}
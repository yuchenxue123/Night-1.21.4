package cute.neko.night.features.module

import cute.neko.night.event.EventListener
import cute.neko.night.event.events.game.misc.KeyboardEvent
import cute.neko.night.event.handle
import cute.neko.night.features.module.combat.ModuleKillAura
import cute.neko.night.features.module.combat.antivelocity.ModuleAntiVelocity
import cute.neko.night.features.module.misc.ModuleAntiBot
import cute.neko.night.features.module.misc.ModuleTest
import cute.neko.night.features.module.misc.disabler.ModuleDisabler
import cute.neko.night.features.module.movement.ModuleNoJumpDelay
import cute.neko.night.features.module.movement.ModuleNoPush
import cute.neko.night.features.module.movement.ModuleSprint
import cute.neko.night.features.module.movement.speed.ModuleSpeed
import cute.neko.night.features.module.player.ModuleBlockFly
import cute.neko.night.features.module.player.ModuleEagle
import cute.neko.night.features.module.player.ModuleInvManager
import cute.neko.night.features.module.player.ModuleStealer
import cute.neko.night.features.module.player.nofall.ModuleNoFall
import cute.neko.night.features.module.render.*
import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/01/12 - 19:10
 */

object ModuleManager : EventListener, Accessor {

    val modules = mutableListOf<cute.neko.night.features.module.ClientModule>()

    fun load() {
        register(
            // combat
            ModuleAntiVelocity,
            ModuleKillAura,

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
    fun register(vararg array: cute.neko.night.features.module.ClientModule) {
        modules.addAll(array)
    }

    /**
     * Register single module
     */
    fun register(module: cute.neko.night.features.module.ClientModule) {
        modules.add(module)
    }

    @Suppress("unused")
    private val onKeyboard = handle<KeyboardEvent> { event ->
        if (mc.currentScreen is ChatScreen) return@handle

        when (event.action) {
            GLFW.GLFW_PRESS -> {
                modules.filter {
                    it.key == event.keyCode && event.key.category == InputUtil.Type.KEYSYM
                }.forEach { module ->
                    module.state = !module.state
                }
            }
        }
    }
}
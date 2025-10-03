package cute.neko.night

import cute.neko.night.config.ConfigSystem
import cute.neko.night.event.EventListener
import cute.neko.night.event.events.game.client.GameInitializeEvent
import cute.neko.night.event.events.game.client.GameShutdownEvent
import cute.neko.night.event.handler
import cute.neko.night.features.command.CommandManager
import cute.neko.night.features.module.ModuleManager
import cute.neko.night.ui.widget.WidgetManager
import cute.neko.night.utils.client.FileUtils
import cute.neko.night.utils.client.KeyboardUtils
import cute.neko.night.utils.lang.LanguageManager
import cute.neko.night.utils.misc.resources.Buffers
import cute.neko.night.utils.render.nano.NanoUtils
import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Night : ModInitializer, EventListener {
    // client info
    // "Stay" -> "Today" -> "Night"
    const val CLIENT_NAME = "Night"
    const val CLIENT_VERSION = "0.1"

    const val MOD_ID = "night"

    val logger: Logger = LogManager.getLogger(Night::class)

    var loaded = false

    override fun onInitialize() {
        println("Night mod entrypoint is loaded...")
    }

    @Suppress("unused")
    private val onInitialize = handler<GameInitializeEvent> {
        FileUtils.create()

        KeyboardUtils.generate()

        NanoUtils.create()

        LanguageManager.load()
        ModuleManager.load()
        WidgetManager.load()

        CommandManager.load()

        ConfigSystem.load("default")

        loaded = true
    }

    @Suppress("unused")
    private val onShutdown = handler<GameShutdownEvent> {
        ConfigSystem.save("default")

        Buffers.release()
        NanoUtils.free()
    }
}
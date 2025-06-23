package cute.neko.night

import cute.neko.night.config.ConfigSystem
import cute.neko.night.features.command.CommandManager
import cute.neko.night.features.module.ModuleManager
import cute.neko.night.ui.widget.WidgetManager
import cute.neko.night.utils.client.FileUtils
import cute.neko.night.utils.client.KeyboardUtils
import cute.neko.night.utils.lang.LanguageManager
import cute.neko.night.utils.nano.NanoManager

object Night {
    // client info
    // "Stay" -> "Today" -> "Night"
    const val CLIENT_NAME = "Night"
    const val CLIENT_VERSION = "0.1"

    const val MOD_ID = "night"

    var loaded = false

    fun initiate() {
        FileUtils.create()

        KeyboardUtils.generate()

        NanoManager.create()

        LanguageManager.load()
        ModuleManager.load()
        WidgetManager.load()

        CommandManager.load()

        ConfigSystem.load("default")

        loaded = true
    }

    fun shutdown() {
        ConfigSystem.save("default")
    }
}
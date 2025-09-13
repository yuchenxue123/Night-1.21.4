package cute.neko.night.features.command.client

import cute.neko.night.features.command.Command
import cute.neko.night.features.module.ModuleManager
import cute.neko.night.utils.client.KeyboardUtils

object BindCommand : Command(
    name = "bind",
    alias = arrayOf("key"),
    usage = ".bind <module> <key>"
) {
    override fun execute(args: Array<String>) {
        when (args.size) {
            3 -> {
                val module = ModuleManager.getModule(args[1])

                if (module == null) {
                    debug.warn("未找到该模块")
                    return
                }

                val key = KeyboardUtils.getByName(args[2])

                module.key = key

                debug.debug("模块 ${module.name} 绑定按键 ${KeyboardUtils.getKeyName(module.key).uppercase()}")
            }

            else ->
                chatUsage()
        }
    }
}
package cute.neko.night.features.command.client

import cute.neko.night.features.command.Command
import cute.neko.night.features.module.ModuleManager

/**
 * @author yuchenxue
 * @date 2025/06/24
 */

object ToggleCommand : Command(
    name = "toggle",
    alias = arrayOf("t"),
    usage = ".toggle <module>",
) {
    override fun execute(args: Array<String>) {
        when (args.size) {
            2 -> {
                val module = ModuleManager.getModule(args[1])

                if (module == null) {
                    debug.warn("未找到该模块")
                    return
                }

                module.toggle()

                debug.debug("模块 ${module.name} 已${if (module.state) "开启" else "关闭"}")
            }

            else ->
                chatUsage()
        }
    }
}
package cute.neko.night.features.command.client

import cute.neko.night.config.ConfigSystem
import cute.neko.night.features.command.Command
import cute.neko.night.utils.client.FileUtils.CONFIG_DIR
import java.awt.Desktop

/**
 * @author yuchenxue
 * @date 2025/07/11
 */

object ConfigCommand : Command(
    name = "config",
    alias = arrayOf("cfg"),
    usage = ".config <load/save/delete/list/open> <name>"
) {
    override fun execute(args: Array<String>) {
        when (args.size) {
            3 -> {
                when (args[1]) {
                    "load" -> {
                        if (!ConfigSystem.load(args[2])) {
                            debug.error("配置 ${args[2]} 不存在或加载失败")
                        } else {
                            debug.debug("配置 ${args[2]} 加载成功")
                        }
                    }

                    "save" -> {
                        if (!ConfigSystem.save(args[2])) {
                            debug.error("配置 ${args[2]} 保存失败")
                        } else {
                            debug.debug("配置 ${args[2]} 保存成功")
                        }
                    }

                    "delete" -> {
                        if (!ConfigSystem.delete(args[2])) {
                            debug.error("配置 ${args[2]} 删除失败")
                        } else {
                            debug.debug("配置 ${args[2]} 删除成功")
                        }
                    }

                    else ->
                        chatUsage()
                }
            }

            2 -> {
                when (args[1]) {
                    "list" -> {
                        val list = ConfigSystem.list().joinToString(", ")

                        if (list.isEmpty()) {
                            debug.warn("配置文件夹为空")
                        }

                        debug.debug("配置列表: $list")
                    }

                    "open" -> {
                        if (!CONFIG_DIR.exists()) {
                            debug.error("配置文件夹不存在")
                        }

                        val os = System.getProperty("os.name").lowercase()
                        val path = CONFIG_DIR.canonicalPath

                        when {
                            os.contains("win") -> ProcessBuilder("explorer.exe", path).start()
                            os.contains("mac") -> ProcessBuilder("open", path).start()
                            os.contains("nix") || os.contains("nux") -> ProcessBuilder("xdg-open", path).start()
                            else -> debug.error("不支持的操作系统")
                        }
                    }

                    else ->
                        chatUsage()
                }
            }

            else ->
                chatUsage()
        }
    }
}
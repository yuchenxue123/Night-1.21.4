package cute.neko.night.features.command

import cute.neko.event.EventListener
import cute.neko.night.features.command.client.BindCommand
import cute.neko.night.features.command.client.ConfigCommand
import cute.neko.night.features.command.client.ToggleCommand
import cute.neko.night.features.command.game.GameModeCommand
import cute.neko.night.utils.misc.debug.Debug

/**
 * @author yuchenxue
 * @date 2025/01/13 - 14:34
 */

object CommandManager : EventListener {
    val commands = mutableListOf<Command>()

    // command prefix
    private const val PREFIX = '.'

    // debug
    private val debug = Debug.create(Debug.Type.CLIENT)

    fun load() {
        register(
            BindCommand,
            ToggleCommand,
            ConfigCommand,

            // game
            GameModeCommand,
        )
    }

    /**
     * Receive a chat message and execute it,
     * if it is a fair command will run client command.
     */
    fun receiveInput(input: String) {
        if (input.length == 1 && input[0] == PREFIX) {
            debug.error("不合法的空指令")
            return
        }

        val args = parse(input.substring(1))

        val command = commands.find { it.name.equals(args[0], true) } ?: run {
            debug.error("未找到该指令")
            return
        }

        command.execute(args)
    }

    /**
     * Predict execute
     */
    fun predictExecute(input: String): Boolean {
        return input.startsWith(PREFIX)
    }

    /**
     * Parse command
     */
    private fun parse(input: String): Array<String> {
        val args = mutableListOf<String>()
        val arg = StringBuilder()
        var quote = false
        var escape = false

        for (c in input) {
            when {
                escape -> {
                    arg.append(c)
                    escape = false
                }

                c == '\\' -> {
                    escape = true
                }

                c == '"' -> {
                    quote = !quote
                }

                c == ' ' && !quote -> {
                    if (arg.isNotEmpty()) {
                        args.add(arg.toString())
                        arg.clear()
                    }
                }

                else -> {
                    arg.append(c)
                }
            }
        }

        if (arg.isNotEmpty()) {
            args.add(arg.toString())
        }

        return args.toTypedArray()
    }

    /**
     * Register commands
     */
    fun register(vararg array: Command) {
        commands.addAll(array)
    }

    /**
     * Register a command
     */
    fun register(command: Command) {
        commands.add(command)
    }
}
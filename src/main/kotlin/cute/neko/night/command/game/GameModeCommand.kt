package cute.neko.night.command.game

import cute.neko.night.command.Command
import cute.neko.night.utils.client.network

/**
 * @author yuchenxue
 * @date 2025/06/01
 */

object GameModeCommand : Command(
    name = "gm",
    usage = ".gm <0/1/2/3>"
) {
    override fun execute(args: Array<String>) {
        when (args.size) {
            2 -> {
                when (args[1]) {
                    "0" -> network.sendChatCommand("gamemode survival")

                    "1" -> network.sendChatCommand("gamemode creative")

                    "2" -> network.sendChatCommand("gamemode adventure")

                    "3" -> network.sendChatCommand("gamemode spectator")

                    else -> debug.error("没有该模式")
                }
            }

            else -> chatUsage()
        }
    }
}
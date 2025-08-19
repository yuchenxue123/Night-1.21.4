package cute.neko.night.features.module.misc.chat

import cute.neko.night.event.events.game.misc.ChatSendEvent
import cute.neko.night.event.handler
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory

/**
 * @author yuchenxue
 * @date 2025/08/19
 * @description 连接本地开的IRC服务端，在没有/tell指令的服务器上给自己MCC机器人发送指令
 */

object ModuleChat : ClientModule(
    "Chat",
    ModuleCategory.MISC
) {

    private val client = ChatClient()

    override fun enable() {
        client.connect("127.0.0.1", 6667, player.gameProfile.name)
    }

    override fun disable() {
        client.disconnect()
    }

    @Suppress("unused")
    private val onChatSend = handler<ChatSendEvent> { event ->
        val message = event.message

        if (message.startsWith("-")) {
            client.send(message.substring(1))
            event.cancel()
        }
    }
}
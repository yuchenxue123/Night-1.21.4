package cute.neko.night.utils.client

import cute.neko.night.utils.extensions.space
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.Text

/**
 * @author yuchenxue
 * @date 2025/01/12 - 20:10
 */

// minecraft instance
val mc: MinecraftClient
    inline get() = MinecraftClient.getInstance()

// client player
val player: ClientPlayerEntity
    inline get() = mc.player!!

// client world
val world: ClientWorld
    inline get() = mc.world!!

val network: ClientPlayNetworkHandler
    get() = mc.networkHandler!!

/**
 * Add a new chat message to ChatHud
 */
fun chat(vararg texts: Text) {
    val empty = Text.empty()

    // join space
    texts.forEachIndexed { index, text ->
        empty.append(text)

        if (index != texts.lastIndex) {
            empty.space()
        }
    }

    if (mc.player == null) {
        return
    }

    mc.inGameHud.chatHud.addMessage(empty)
}

fun chat(text: String) {
    chat(Text.of(text))
}
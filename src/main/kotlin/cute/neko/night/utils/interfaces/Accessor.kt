package cute.neko.night.utils.interfaces

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld

/**
 * @author yuchenxue
 * @date 2025/01/13 - 11:46
 */

interface Accessor {
    val mc: MinecraftClient
        get() = MinecraftClient.getInstance()
    val player: ClientPlayerEntity
        get() = mc.player!!
    val world: ClientWorld
        get() = mc.world!!
    val interactionManager
        get() = mc.interactionManager!!
}
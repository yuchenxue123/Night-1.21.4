package cute.neko.night.utils.interfaces

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld

interface Accessor {
    val mc: MinecraftClient
        get() = MinecraftClient.getInstance()
    val player: ClientPlayerEntity
        get() = mc.player!!
    val world: ClientWorld
        get() = mc.world!!
    val network: ClientPlayNetworkHandler
        get() = mc.networkHandler!!
    val interactionManager
        get() = mc.interactionManager!!
}
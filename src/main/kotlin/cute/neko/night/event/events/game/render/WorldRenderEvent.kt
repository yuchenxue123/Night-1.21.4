package cute.neko.night.event.events.game.render

import cute.neko.night.event.Event
import net.minecraft.client.render.Camera
import net.minecraft.client.util.math.MatrixStack

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

class WorldRenderEvent(val matrixStack: MatrixStack, val camera: Camera, val partialTicks: Float) : Event()
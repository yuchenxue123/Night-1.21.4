package cute.neko.night.event.events.game.render

import cute.neko.night.event.Event
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.Window

class ScreenRenderEvent(
    val context: DrawContext,
    val window: Window,
    val tickDelta: Float
) : Event
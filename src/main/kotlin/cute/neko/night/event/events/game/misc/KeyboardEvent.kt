package cute.neko.night.event.events.game.misc

import cute.neko.night.event.Event
import net.minecraft.client.util.InputUtil.Key

class KeyboardEvent(
    val key: Key,
    val keyCode: Int,
    val scanCode: Int,
    val action: Int
): Event
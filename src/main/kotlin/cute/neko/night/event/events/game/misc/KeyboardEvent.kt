package cute.neko.night.event.events.game.misc

import cute.neko.night.event.Event
import net.minecraft.client.util.InputUtil.Key

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

class KeyboardEvent(
    val key: Key,
    val keyCode: Int,
    val scanCode: Int,
    val action: Int
): Event
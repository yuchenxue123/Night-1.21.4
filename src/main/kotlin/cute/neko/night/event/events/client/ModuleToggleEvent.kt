package cute.neko.night.event.events.client

import cute.neko.night.event.Event
import cute.neko.night.features.module.ClientModule

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

class ModuleToggleEvent(
    val module: ClientModule,
    val state: Boolean
) : Event
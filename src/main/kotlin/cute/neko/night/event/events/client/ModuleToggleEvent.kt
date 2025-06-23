package cute.neko.night.event.events.client

import cute.neko.night.event.Event

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

class ModuleToggleEvent(
    val module: cute.neko.night.features.module.ClientModule,
    val state: Boolean
) : Event()
package cute.neko.night.event.events.client

import cute.neko.night.event.Event
import cute.neko.night.features.module.ClientModule

class ModuleToggleEvent(
    val module: ClientModule,
    val state: Boolean
) : Event
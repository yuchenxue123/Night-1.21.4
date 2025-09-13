package cute.neko.night.event.events.game.misc

import cute.neko.night.event.CancellableEvent

class ChatSendEvent(val message: String) : CancellableEvent()
package cute.neko.night.mod.mousetweaks

import cute.neko.night.event.Event
import net.minecraft.client.util.InputUtil

class HandledScreenRenderEvent(val mouseX: Double, val mouseY: Double) : Event

class HandledScreenMouseInputEvent(val button: MouseButton, val type: Type) : Event {

    constructor(button: Int, type: Type) : this(MouseButton.parse(button), type)

    enum class Type {
        CLICKED,
        RELEASED,
        DRAGGED
    }
}

class HandledScreenKeyboardInputEvent(
    private val keycode: Int,
    private val scancode: Int
) : Event {

    fun matches(key: Int): Boolean {
        val key = InputUtil.Type.KEYSYM.createFromCode(key)

        return if (keycode == InputUtil.UNKNOWN_KEY.code)
            key.category == InputUtil.Type.SCANCODE && key.code == scancode
        else
            key.category == InputUtil.Type.KEYSYM && key.code == keycode
    }
}
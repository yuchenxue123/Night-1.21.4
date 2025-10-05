package cute.neko.night.features.module

import cute.neko.night.event.EventListener
import cute.neko.night.event.EventManager
import cute.neko.night.event.events.client.ModuleToggleEvent
import cute.neko.night.event.removeEventListenerScope
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.Toggleable
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable
import cute.neko.night.utils.client.inGame
import cute.neko.night.utils.lang.LanguageManager
import cute.neko.night.utils.lang.translate
import cute.neko.night.utils.misc.debug.Debug
import org.lwjgl.glfw.GLFW

open class ClientModule(
    override val name: String,
    val category: ModuleCategory,
    var key: Int = GLFW.GLFW_KEY_UNKNOWN,
    val locked: Boolean = false,
    var hidden: Boolean = false,
    state: Boolean = false,
) : Configurable(name), Toggleable, EventListener {

    val showName: String
        get() {
            val key = "neko.module.$name.name"

            return when {
                LanguageManager.hasTranslate(key) -> translate(key)
                else -> name
            }
        }

    val description: String
        get() = translate("neko.module.$name.desc")

    open val suffix: String = ""

    protected val debug = Debug(Debug.Type.MODULE)

    var state = state
        set(newState) {
            if (newState == field) return

            field = newState

            this.onToggled(newState)
        }

    fun toggle() {
        state = !state
    }

    override fun onToggled(state: Boolean): Boolean {
        if (!inGame) {
            return state
        }

        if (!state) {
            runCatching {
                // Remove and cancel coroutine scope
                removeEventListenerScope()
            }.onFailure {
                error("failed cancel sequences: $it")
            }
        }

        val state = super.onToggled(state)
        inner.filterIsInstance<Toggleable>().forEach { it.onToggled(state) }

        // Call module toggle event
        EventManager.callEvent(ModuleToggleEvent(this, state))
        return state
    }

    protected fun <T: Choice> choices(
        name: String,
        array: Array<T>,
        default: T = array[0]
    ) = ChoicesConfigurable(name, array, default, parent = this).also {
        tree(it)
    }

    override val running: Boolean
        get() = super.running && inGame && state
}
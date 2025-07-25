package cute.neko.night.features.module

import cute.neko.event.EventListener
import cute.neko.event.EventManager
import cute.neko.night.Night
import cute.neko.night.event.events.client.ModuleToggleEvent
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.EmptyConfigurable
import cute.neko.night.features.setting.config.types.ToggleConfigurable
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable
import cute.neko.night.utils.lang.LanguageManager
import cute.neko.night.utils.lang.translate
import cute.neko.night.utils.misc.debug.Debug
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/01/12 - 19:09
 */

open class ClientModule(
    override val name: String,
    val category: ModuleCategory,
    var key: Int = GLFW.GLFW_KEY_UNKNOWN,
    val locked: Boolean = false,
    var hidden: Boolean = false,
) : Configurable(name), EventListener {

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

    var state = false
        set(new) {
            if (new == field) return

            field = new

            if (new) enable() else disable()

            // module toggle event
            if (Night.loaded) {
                EventManager.callEvent(ModuleToggleEvent(this, new))
            }

            inner.filterIsInstance<ChoicesConfigurable<*>>().forEach { it.newState(new) }
            inner.filterIsInstance<ToggleConfigurable>().forEach { it.newState(new) }
            inner.filterIsInstance<EmptyConfigurable>().forEach { it.newState(new) }
        }

    open fun enable() {}
    open fun disable() {}

    fun toggle() {
        state = !state
    }

    protected fun <T: Choice> choices(
        name: String,
        array: Array<T>,
        default: T = array[0]
    ) = ChoicesConfigurable(name, array, default, parent = this).also {
        tree(it)
    }

    override val running: Boolean
        get() = super.running && state
}
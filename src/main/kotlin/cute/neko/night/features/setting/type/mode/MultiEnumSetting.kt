package cute.neko.night.features.setting.type.mode

import cute.neko.night.features.setting.MutableSetting

class MultiEnumSetting<E>(
    name: String,
    private val enums: Array<E>,
    value: E,
    visibility: () -> Boolean = { true }
) : MutableSetting<E>(name, value, visibility) where E : SubMode, E : Enum<E> {

    val activities = mutableListOf<E>()

    fun getActivesArray(): Array<String> {
        return activities.map { it.modeName }.toTypedArray()
    }

    val selects: Array<String>
        get() = enums.map { it.modeName }.toTypedArray()

    fun active(enum: E) {
        if (activities.contains(enum)) return

        activities.add(enum)
    }

    fun active(name: String) {
        val find = enums.find { it.modeName.equals(name, true) } ?: return

        if (activities.contains(find)) return

        activities.add(find)
    }

    fun toggle(name: String) {
        val find = enums.find { it.modeName.equals(name, true) } ?: return

        if (activities.contains(find)) {
            activities.remove(find)
            return
        }

        activities.add(find)
    }

    fun isActive(name: String): Boolean {
        return activities.find { it.modeName.equals(name, true) } != null
    }

    fun isActive(enum: E): Boolean {
        return activities.contains(enum)
    }

    fun activeAll() = apply {
        enums.forEach(::active)
    }
}
package cute.neko.night.features.setting.type.mode

class EnumSetting<E>(
    name: String,
    private val enums: Array<E>,
    value: E,
    visibility: () -> Boolean = { true }
) : AbstractModeSetting<E>(name, value, visibility) where E : SubMode, E : Enum<E> {

    override val modes: Array<String>
        get() = enums.map { it.modeName }.toTypedArray()

    fun set(value: String) {
        val new = enums.find { it.name.equals(value, true) } ?: enums[0]
        this.set(new)
    }

    override fun getAsString(): String = value.modeName

    override fun setByString(value: String) {
        this.set(value)
    }
}
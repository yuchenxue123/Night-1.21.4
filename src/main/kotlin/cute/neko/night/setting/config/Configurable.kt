package cute.neko.night.setting.config

import cute.neko.night.setting.AbstractSetting
import cute.neko.night.setting.type.mode.EnumSetting
import cute.neko.night.setting.type.mode.ModeSetting
import cute.neko.night.setting.type.mode.MultiEnumSetting
import cute.neko.night.setting.type.mode.SubMode
import cute.neko.night.setting.type.number.FloatSetting
import cute.neko.night.setting.type.number.IntegerSetting
import cute.neko.night.setting.type.primitive.BooleanSetting
import cute.neko.night.utils.interfaces.Nameable

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

open class Configurable(override val name: String) : Nameable {
    private val _settings = mutableListOf<AbstractSetting<*>>()

    open val settings: MutableList<AbstractSetting<*>>
        get() = _settings

    fun boolean(
        name: String,
        value: Boolean,
        visibility: () -> Boolean = { true }
    ) = setting(BooleanSetting(name, value, visibility))

    fun int(
        name: String,
        value: Int,
        range: IntRange = 1..20,
        step: Int = 1,
        visibility: () -> Boolean = { true }
    ) = setting(IntegerSetting(name, value, range, step, visibility))

    fun float(
        name: String,
        value: Float,
        range: ClosedFloatingPointRange<Float> = 1f..7f,
        step: Float = 0.5f,
        visibility: () -> Boolean = { true }
    ) = setting(FloatSetting(name, value, range, step, visibility))

    inline fun <reified E> mode(
        name: String,
        value: E,
        noinline visibility: () -> Boolean = { true }
    ) where E : Enum<E>, E : SubMode = setting(EnumSetting(name, enumValues<E>(), value, visibility))

    fun mode(
        name: String,
        modes: Array<String>,
        value: String = modes[0],
        visibility: () -> Boolean = { true }
    ) = setting(ModeSetting(name, modes, value, visibility))

    fun <E> enum(
        name: String,
        array: Array<E>,
        visibility: () -> Boolean = { true }
    ) where E : Enum<E>, E : SubMode = setting(MultiEnumSetting(name, array, array[0], visibility))

    inline fun <reified E> enum(
        name: String,
        value: E,
        noinline visibility: () -> Boolean = { true }
    ) where E : Enum<E>, E : SubMode = setting(MultiEnumSetting(name, enumValues<E>(), value, visibility))

    fun <S : AbstractSetting<*>> setting(setting: S): S {
        _settings.add(setting)
        return setting
    }

    val inner = mutableListOf<Configurable>()

    protected fun <T : Configurable> tree(config: T): T {
        _settings.addAll(config.settings)
        inner.add(config)
        return config
    }
}
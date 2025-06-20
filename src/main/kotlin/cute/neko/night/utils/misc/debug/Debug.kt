package cute.neko.night.utils.misc.debug

import cute.neko.night.utils.client.chat
import cute.neko.night.utils.extensions.space
import net.minecraft.text.Text
import net.minecraft.util.Formatting

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

private val CLIENT_PREFIX: Text = Text.empty()
    .append(Text.of("["))
    .append(Text.literal("Client").formatted(Formatting.YELLOW))
    .append(Text.of("]"))

private val MODULE_PREFIX: Text = Text.empty()
    .append(Text.of("["))
    .append(Text.literal("Module").formatted(Formatting.AQUA))
    .append(Text.of("]"))

private val COMMAND_PREFIX: Text = Text.empty()
    .append(Text.of("["))
    .append(Text.literal("Command").formatted(Formatting.GREEN))
    .append(Text.of("]"))

class Debug(private val type: Type) {

    companion object {

        fun create(type: Type = Type.DEFAULT): Debug {
            return Debug(type)
        }
    }

    fun debug(msg: String) {
        chat(type.prefix, Text.of(msg))
    }

    fun debug(msg: Text) {
        chat(type.prefix, msg)
    }

    fun warn(msg: String) {
        chat(type.prefix, Text.literal(msg).formatted(Formatting.YELLOW))
    }

    fun warn(msg: Text) {
        chat(type.prefix, Text.empty().formatted(Formatting.YELLOW).append(msg))
    }

    fun error(msg: String) {
        chat(type.prefix, Text.literal(msg).formatted(Formatting.RED))
    }

    fun error(msg: Text) {
        chat(type.prefix, Text.empty().formatted(Formatting.RED).append(msg))
    }

    enum class Type {
        DEFAULT {
            override val prefix: Text
                get() = Text.empty()
        },

        CLIENT {
            override val prefix: Text
                get() = Text.empty()
                    .append(CLIENT_PREFIX)
        },

        MODULE {
            override val prefix: Text
                get() = Text.empty()
                    .append(CLIENT_PREFIX)
                    .space()
                    .append(MODULE_PREFIX)
        },

        COMMAND {
            override val prefix: Text
                get() = Text.empty()
                    .append(CLIENT_PREFIX)
                    .space()
                    .append(COMMAND_PREFIX)
        }
        ;

        abstract val prefix: Text
    }
}
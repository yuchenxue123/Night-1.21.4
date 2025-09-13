package cute.neko.night.utils.client

import org.lwjgl.glfw.GLFW

object KeyboardUtils {

    // letters
    private val PART_1 = 65..90

    // numbers
    private val PART_2 = 48..57

    private val map = mutableMapOf<Int, String>()

    fun generate() {
        map.apply {
            arrayOf(PART_1, PART_2).forEach part@{ part ->
                part.forEach key@{ key ->
                    val name = GLFW.glfwGetKeyName(key, GLFW.GLFW_KEY_UNKNOWN)?.uppercase() ?: return@key
                    this[key] = name
                }
            }

            this[GLFW.GLFW_KEY_LEFT_SHIFT] = "LSHIFT"
            this[GLFW.GLFW_KEY_RIGHT_SHIFT] = "RSHIFT"

            this[GLFW.GLFW_KEY_LEFT_CONTROL] = "LCTRL"
            this[GLFW.GLFW_KEY_RIGHT_CONTROL] = "RCTRL"

            this[GLFW.GLFW_KEY_LEFT_ALT] = "LALT"
            this[GLFW.GLFW_KEY_RIGHT_ALT] = "RALT"

            this[GLFW.GLFW_KEY_SPACE] = "SPACE"
        }
    }

    fun getByName(name: String): Int {
        if (name.equals("none", true)) {
            return GLFW.GLFW_KEY_UNKNOWN
        }

        map.forEach { (n, k) ->
            if (k.equals(name, true)) {
                return n
            }
        }

        return GLFW.GLFW_KEY_UNKNOWN
    }

    fun getKeyName(key: Int): String {
        return map[key] ?: "NONE"
    }
}
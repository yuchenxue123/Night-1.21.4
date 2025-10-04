package cute.neko.night.mod.mousetweaks

/**
 * @author yuchenxue
 * @date 2025/09/01
 */

enum class MouseButton(val id: Int) {
    LEFT(0),
    RIGHT(1),
    MIDDLE(2),
    UNKNOWN(-1)

    ;

    companion object {
        fun parse(id: Int): MouseButton {
            return entries.find { it.id == id } ?: UNKNOWN
        }
    }
}
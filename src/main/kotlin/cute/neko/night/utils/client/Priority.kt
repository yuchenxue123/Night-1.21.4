package cute.neko.night.utils.client

/**
 * @author yuchenxue
 * @date 2025/05/31
 */

enum class Priority(val value: Int) {
    NONE(-10000),

    // rotation in 100-500


    ROTATION_HIGH(500),

    ROTATION_NORMAL(250),

    ROTATION_LOW(100),

    ;

    companion object {
        fun parse(value: Int): Priority {
            return entries.find { it.value == value } ?: NONE
        }
    }
}
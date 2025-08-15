package cute.neko.night.event.priorities

/**
 * @author yuchenxue
 * @date 2025/07/25
 */

enum class Priorities(override val value: Int) : EventPriority {

    FINAL(0),

    NOT_IMPORTANT(50),

    DEFAULT(100),

    IMPORTANT(150),

    FIRST_PRIORITY(999),
}
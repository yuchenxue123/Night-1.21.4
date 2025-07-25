package cute.neko.night.event

import cute.neko.event.api.EventPriority

/**
 * @author yuchenxue
 * @date 2025/07/25
 */

enum class Priorities(override val value: Int) : EventPriority {

    FINAL(0),

    NOT_IMPORTANT(50),

    // DEFAULT -> DefaultPriority.DEFAULT == 100

    IMPORTANT(150),
}
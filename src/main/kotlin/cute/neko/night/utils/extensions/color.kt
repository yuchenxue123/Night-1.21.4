package cute.neko.night.utils.extensions

import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

val Color.redF: Float
    get() = red / 255f

val Color.greenF: Float
    get() = green / 255f

val Color.blueF: Float
    get() = blue / 255f

val Color.alphaF: Float
    get() = alpha / 255f

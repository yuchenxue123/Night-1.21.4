package cute.neko.night.utils.render.context

import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/07/26
 */

interface RenderContext {

    fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Color)

    fun drawRoundRect(x: Float, y: Float, width: Float, height: Float, radius: Float, color: Color)


}
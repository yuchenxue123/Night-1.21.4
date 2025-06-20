package cute.neko.night.utils.extensions

import net.minecraft.client.gui.DrawContext

/**
 * @author yuchenxue
 * @date 2025/01/13 - 14:19
 */

fun DrawContext.fillRect(x: Int, y: Int, width: Int, height: Int, color: Int) =
    this.fill(x, y, x + width, y + height, color)
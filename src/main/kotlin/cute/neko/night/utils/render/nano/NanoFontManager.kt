package cute.neko.night.utils.render.nano

import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.misc.resources.Resource
import cute.neko.night.utils.render.font.NanoFont
import cute.neko.night.utils.render.nano.NanoUtils.nvg
import org.lwjgl.nanovg.NanoVG

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

object NanoFontManager : Accessor {
    private val fonts = mutableListOf<NanoFont>()

    val ROBOTO = createFont("roboto", "roboto.ttf")
    val GENSHIN = createFont("genshin", "genshin.ttf")
    val GENSHIN_15 = createFont("genshin_15", "genshin.ttf", 15f)

    private fun createFont(name: String, fontFileName: String, size: Float = 18f): NanoFont {
        var font = -1

        Resource.font(fontFileName).withBuffer { buffer ->
            font = NanoVG.nvgCreateFontMem(nvg, name, buffer, true)
        }

        val create = NanoFont(name, font, size)
        fonts.add(create)

        return create
    }
}
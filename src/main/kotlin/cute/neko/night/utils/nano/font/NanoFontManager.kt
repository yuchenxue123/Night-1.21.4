package cute.neko.night.utils.nano.font

import cute.neko.night.utils.interfaces.Accessor

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

object NanoFontManager : Accessor {
    val fonts = mutableListOf<NanoFont>()

    val ROBOTO = createFont("roboto", "roboto.ttf")
    val GENSHIN = createFont("genshin", "genshin.ttf")
    val GENSHIN_15 = createFont("genshin_15", "genshin.ttf", 15f)

    private fun createFont(name: String, fontFileName: String, size: Float = 18f): NanoFont {
        val create = NanoFont(FontData(name, fontFileName), size)
        fonts.add(create)
        return create
    }
}
package cute.neko.night.utils.nano.font

import cute.neko.night.utils.misc.AssetsManager
import java.nio.ByteBuffer

/**
 * @author yuchenxue
 * @date 2025/05/08
 */

data class FontData(val name: String, val fileName: String) {

    val buffer: ByteBuffer?
        get() = AssetsManager.getFontAsByteBuffer(fileName)
}
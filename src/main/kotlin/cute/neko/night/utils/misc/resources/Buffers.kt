package cute.neko.night.utils.misc.resources

import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

/**
 * @author yuchenxue
 * @date 2025/08/02
 */

object Buffers {

    private val buffers = mutableListOf<ByteBuffer>()

    fun put(buffer: ByteBuffer) {
        buffers += buffer
    }

    fun release() {
        buffers.removeIf { buffer ->
            MemoryUtil.memFree(buffer)
            true
        }
    }
}
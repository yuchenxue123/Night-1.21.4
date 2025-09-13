package cute.neko.night.utils.misc.resources

import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

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
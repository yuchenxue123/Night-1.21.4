package cute.neko.night.utils.misc.resources

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import cute.neko.night.utils.client.FileUtils.gson
import cute.neko.night.utils.client.mc
import net.minecraft.util.Identifier
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

open class Resource(private val path: String) {

    companion object {

        const val BASE_PATH = "assets/neko"

        fun font(fileName: String): BufferResource {
            return BufferResource("$BASE_PATH/font/$fileName")
        }

        fun image(fileName: String): BufferResource {
            return BufferResource("$BASE_PATH/image/$fileName")
        }

        fun language(fileName: String): TextResource {
            return TextResource("$BASE_PATH/data/lang/$fileName")
        }

        fun identifier(id: Identifier): IdentifierResource {
            return IdentifierResource(id)
        }
    }

    class BufferResource(path: String) : Resource(path) {

        private val buffer: ByteBuffer? by lazy {
            javaClass.classLoader.getResourceAsStream(path)?.use { stream ->
                val bytes = stream.readAllBytes()
                val buffer = MemoryUtil.memAlloc(bytes.size)
                buffer.put(bytes)
                buffer.flip()
            }
        }

        fun withBuffer(block: (ByteBuffer) -> Unit) = apply {
            buffer?.let {
                block.invoke(it)
                Buffers.put(it)
            }
        }
    }

    class IdentifierResource(id: Identifier) : Resource(id.path) {

        private val buffer: ByteBuffer? by lazy {
            val resource = mc.resourceManager.getResource(id)

            if (!resource.isPresent) {
                return@lazy null
            }

            resource.get().inputStream.use { stream ->
                val bytes = stream.readAllBytes()
                val buffer = MemoryUtil.memAlloc(bytes.size)
                buffer.put(bytes)
                buffer.flip()
            }
        }

        fun withBuffer(block: (ByteBuffer) -> Unit) = apply {
            buffer?.let {
                block.invoke(it)
                Buffers.put(it)
            }
        }
    }

    class TextResource(path: String) : Resource(path) {
        private val context: String by lazy {
            javaClass.classLoader.getResourceAsStream(path)
                ?.bufferedReader()
                ?.use { it.readText() }
                ?: ""
        }

        fun toJson(): JsonObject {
            if (context.isEmpty()) {
                return JsonObject()
            }

            return try {
                gson.fromJson(context, JsonObject::class.java)
            } catch (_: JsonSyntaxException) {
                JsonObject()
            }
        }
    }
}
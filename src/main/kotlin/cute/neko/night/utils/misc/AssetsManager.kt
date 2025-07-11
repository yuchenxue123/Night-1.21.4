package cute.neko.night.utils.misc

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import cute.neko.night.utils.client.FileUtils.gson
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * @author yuchenxue
 * @date 2025/05/08
 */

object AssetsManager {

    private const val BASE_PATH = "assets/neko"
    private const val FONT_PATH = "$BASE_PATH/font"
    private const val IMAGE_PATH = "$BASE_PATH/image"
    private const val LANGUAGE_PATH = "$BASE_PATH/data/lang"


    /**
     * 获取字体作为 [InputStream]
     * @param fileName 字体文件名
     */
    fun getFontAsStream(fileName: String): InputStream? {
        return javaClass.classLoader.getResourceAsStream("$FONT_PATH/$fileName")
    }

    fun getImageAsStream(fileName: String): InputStream? {
        return javaClass.classLoader.getResourceAsStream("$IMAGE_PATH/$fileName")
    }

    /**
     * 获取字体作为 [ByteBuffer]
     * @param fileName 字体文件名
     */
    fun getFontAsByteBuffer(fileName: String): ByteBuffer? {
        return getFontAsStream(fileName)?.use { stream ->
            val readAllBytes = stream.readAllBytes() ?: return null
            MemoryUtil.memAlloc(readAllBytes.size).apply {
                put(readAllBytes)
                flip()
            }
        }
    }

    fun getImageAsByteBuffer(fileName: String): ByteBuffer? {
        return getImageAsStream(fileName)?.use { stream ->
            val readAllBytes = stream.readAllBytes() ?: return null
            MemoryUtil.memAlloc(readAllBytes.size).apply {
                put(readAllBytes)
                flip()
            }
        }
    }

    /**
     * 获取资源作为 [String]
     * @param path 完整资源路径
     */
    fun getAsString(path: String): String {
        return javaClass.classLoader.getResourceAsStream(path)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: ""
    }

    /**
     * 获取语言作为 [JsonObject]
     * @param fileName 语言文件名称
     */
    fun getLanguageAsJsonObject(fileName: String): JsonObject {
        val text = getAsString("$LANGUAGE_PATH/$fileName")

        if (text.isEmpty()) return JsonObject()

        return try {
            gson.fromJson(text, JsonObject::class.java)
        } catch (e: JsonSyntaxException) {
            JsonObject()
        }
    }
}
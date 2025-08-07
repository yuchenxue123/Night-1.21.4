package cute.neko.night.utils.render.nano.image

import cute.neko.night.utils.client.mc
import cute.neko.night.utils.misc.resources.Resource
import cute.neko.night.utils.render.nano.NanoUtils.nvg
import net.minecraft.util.Identifier
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.nanovg.NanoVGGL3
import org.lwjgl.stb.STBImage

/**
 * @author yuchenxue
 * @date 2025/08/03
 */

object NanoImageManager {

    val images = mutableMapOf<String, NanoImage>()
    val texture_to_images = mutableMapOf<Int, NanoImage>()

    fun getOrCreate(fileName: String): NanoImage {
        if (images.containsKey(fileName)) {
            return images[fileName]!!
        }

        var image = 0
        val width = IntArray(1)
        val height = IntArray(1)
        val channels = IntArray(1)

        Resource.image(fileName).withBuffer { buffer ->

            val buffer = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4)

            if (buffer != null) {
                image = NanoVG.nvgCreateImageRGBA(
                    nvg,
                    width[0], height[0],
                    NanoVG.NVG_IMAGE_REPEATX or NanoVG.NVG_IMAGE_REPEATY or NanoVG.NVG_IMAGE_GENERATE_MIPMAPS,
                    buffer
                )
            }
        }

        if (image == 0) {
            throw RuntimeException("Image $fileName could not be loaded.")
        }

        val nanoImage = NanoImage(image, width[0], height[0])
        images[fileName] = nanoImage

        return nanoImage
    }

    fun getOrCreate(texture: Int, width: Int, height: Int): NanoImage {
        if (texture_to_images.containsKey(texture)) {
            return texture_to_images[texture]!!
        }

        val image = NanoVGGL3.nvglCreateImageFromHandle(nvg, texture, width, height, 0)

        if (image == 0) {
            throw RuntimeException("Texture $texture could not be loaded.")
        }

        val nanoImage = NanoImage(image, width, height)
        texture_to_images[texture] = nanoImage

        return nanoImage
    }
}
package cute.neko.night.utils.render.utils

import com.mojang.blaze3d.platform.GlStateManager
import cute.neko.night.utils.client.mc
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL30

/**
 * @author yuchenxue
 * @date 2025/08/07
 */

object TextureScissor {

    fun scissor(texture: Int, x: Int, y: Int, width: Int, height: Int): Int {
        val newTex = GlStateManager._genTexture()
        GlStateManager._bindTexture(newTex)

        GlStateManager._texImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA,
            width, height,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            null
        )

        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)

        val fbo = GlStateManager.glGenFramebuffers()
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo)
//        GlStateManager._clearColor(0f, 0f, 0f, 0f)
//        GlStateManager._clear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        GlStateManager._glFramebufferTexture2D(
            GL30.GL_FRAMEBUFFER,
            GL30.GL_COLOR_ATTACHMENT0,
            GL11.GL_TEXTURE_2D,
            texture,
            0
        )

        GlStateManager._bindTexture(newTex)
        GlStateManager._glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0,  x, y, width, height)

        mc.framebuffer.beginWrite(false)
//        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)

        GlStateManager._glDeleteFramebuffers(fbo)

        return newTex
    }

}
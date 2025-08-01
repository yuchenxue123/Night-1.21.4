package cute.neko.night.utils.render.utils

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

/**
 * @author yuchenxue
 * @date 2025/07/26
 */

class FrameBuffer(
    private var width: Int,
    private var height: Int
) {

    init {
        create()
    }

    private var framebuffer = 0
    private var texture = 0
    private var depth = 0

    fun create() {
        texture = GlStateManager._genTexture()
        GlStateManager._bindTexture(texture)
        GlStateManager._texImage2D(
            GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
            width, height, 0,
            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null
        )
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)

        depth = GlStateManager.glGenRenderbuffers()
        GlStateManager._glBindRenderbuffer(GL30.GL_RENDERBUFFER, depth)
        GlStateManager._glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT24, width, height)

        framebuffer = GlStateManager.glGenFramebuffers()
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer)
        GlStateManager._glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0)
        GlStateManager._glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depth)

        check(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE) {
            "Framebuffer create failed!"
        }

        GlStateManager._glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0)
    }

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height

        GlStateManager._bindTexture(texture)
        GlStateManager._texImage2D(
            GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
            width, height, 0,
            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null
        )

        GlStateManager._glBindRenderbuffer(GL30.GL_RENDERBUFFER, depth)
        GlStateManager._glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT24, width, height)

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer)
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0)
        GlStateManager._glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depth)

        check(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE) {
            "Framebuffer resize failed!"
        }

        GlStateManager._glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0)
    }

    fun bind() {
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer)
        GlStateManager._viewport(0, 0, width, height)
        GlStateManager._clearColor(0f, 0f, 0f, 0f)
        GlStateManager._clear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    fun unbind() {
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)
    }

    fun destroy() {
        GlStateManager._deleteTexture(texture)
        GlStateManager._glDeleteBuffers(depth)
        GlStateManager._glDeleteFramebuffers(framebuffer)
    }

    fun texture(): Int = texture

    fun width(): Int = width

    fun height(): Int = height
}
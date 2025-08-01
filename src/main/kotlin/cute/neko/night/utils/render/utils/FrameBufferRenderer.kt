package cute.neko.night.utils.render.utils

import com.mojang.blaze3d.systems.RenderSystem
import cute.neko.event.EventManager
import cute.neko.night.event.events.game.render.ScreenRenderEvent
import cute.neko.night.utils.client.mc
import cute.neko.night.utils.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.Window
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/07/28
 */

object FrameBufferRenderer {

    val frameBuffer = FrameBuffer(mc.window.framebufferWidth, mc.window.framebufferHeight)

    fun render(context: DrawContext, window: Window, deltaTime: Float) {

        frameBuffer.bind()

        EventManager.callEvent(ScreenRenderEvent(context, window, deltaTime))

        frameBuffer.unbind()

        context.matrices.push()

        GL11.glEnable( GL11.GL_TEXTURE_2D)
        GL11.glBindTexture( GL11.GL_TEXTURE_2D, frameBuffer.texture())

        GL11.glEnable( GL11.GL_BLEND)
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA,  GL11.GL_ONE_MINUS_SRC_ALPHA)

        GL11.glBegin( GL11.GL_QUADS)
        GL11.glTexCoord2f(0f, 0f);  GL11.glVertex2f(0f, 0f)                            // 左下
        GL11.glTexCoord2f(1f, 0f);  GL11.glVertex2f(frameBuffer.width().toFloat(), 0f)        // 右下
        GL11.glTexCoord2f(1f, 1f);  GL11.glVertex2f(frameBuffer.width().toFloat(), frameBuffer.height().toFloat())  // 右上
        GL11.glTexCoord2f(0f, 1f);  GL11.glVertex2f(0f, frameBuffer.height().toFloat())       // 左上
        GL11.glEnd()

        GL11.glDisable( GL11.GL_TEXTURE_2D)
        GL11.glDisable( GL11.GL_BLEND)

        context.matrices.pop()

        RenderSystem.enableBlend()
        RenderSystem.disableDepthTest()
        RenderSystem.depthMask(false)
        RenderSystem.bindTexture(frameBuffer.texture())

        val tessellator = RenderSystem.renderThreadTesselator()
        val buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)

        buffer.vertex(0f, frameBuffer.height().toFloat(), 0f).texture(0f, 1f)
        buffer.vertex(frameBuffer.width().toFloat(), frameBuffer.height().toFloat(), 0f).texture(1f, 1f)
        buffer.vertex(frameBuffer.width().toFloat(), 0f, 0f).texture(1f, 0f)
        buffer.vertex(0f, 0f, 0f).texture(0f, 0f)

        BufferRenderer.drawWithGlobalProgram(buffer.end())

        RenderSystem.depthMask(true)
        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()

        mc.options.fullscreen
    }

    fun resize(width: Int, height: Int) {
        frameBuffer.resize(width, height)
    }
}
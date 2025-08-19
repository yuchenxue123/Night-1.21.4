package cute.neko.night.features.module.misc.chat

import cute.neko.night.utils.client.chat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets


/**
 * @author yuchenxue
 * @date 2025/08/19
 */

class ChatClient {

    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    fun connect(host: String, port: Int, username: String) {
        try {
            socket = Socket(host, port)
            output = PrintWriter(socket!!.getOutputStream(), true, StandardCharsets.UTF_8)
            input = BufferedReader(InputStreamReader(socket!!.getInputStream(), StandardCharsets.UTF_8))

            output?.println(username)

            Thread {
                var line: String?
                while ((input?.readLine().also { line = it }) != null) {
                    chat("[chat] " + line!!)
                }
            }.start()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun send(message: String) {
        output?.println(message)
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (_: IOException) {}
    }
}
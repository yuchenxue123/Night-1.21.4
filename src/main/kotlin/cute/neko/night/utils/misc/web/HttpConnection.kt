package cute.neko.night.utils.misc.web

import java.net.HttpURLConnection
import java.net.URI

class HttpConnection(private val url: String) {

    private var method = METHOD.GET
    private var time = 5000

    companion object {
        fun of(url: String): HttpConnection {
            return HttpConnection(url)
        }
    }

    fun result(): String {
        val connection = URI.create(url).toURL().openConnection() as HttpURLConnection
        connection.requestMethod = method.name
        connection.connectTimeout = time
        connection.readTimeout = time

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val text = connection.inputStream.bufferedReader().readText()
            connection.disconnect()
            return text
        } else {
            return ""
        }
    }

    fun method(method: METHOD) {
        this.method = method
    }

    fun time(time: Int) {
        this.time = time
    }

    enum class METHOD {
        GET,
        POST
    }
}

fun connection(url: String, block: HttpConnection.() -> Unit): HttpConnection {
    val httpConnection = HttpConnection.of(url)

    block.invoke(httpConnection)



    return httpConnection
}
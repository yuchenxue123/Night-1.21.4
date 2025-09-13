package cute.neko.night.utils.client

import com.google.gson.Gson
import cute.neko.night.Night
import cute.neko.night.utils.interfaces.Accessor
import java.io.File

object FileUtils : Accessor {
    val CLIENT_DIR = File(mc.runDirectory, Night.CLIENT_NAME)
    val CONFIG_DIR = File(CLIENT_DIR, "configs")

    // gson
    val gson = Gson()

    fun create() {
        if (!CLIENT_DIR.exists()) {
            CLIENT_DIR.mkdirs()
        }

        if (!CONFIG_DIR.exists()) {
            CONFIG_DIR.mkdirs()
        }
    }
}
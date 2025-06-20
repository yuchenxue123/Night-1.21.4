package cute.neko.night.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import cute.neko.night.utils.client.FileUtils
import cute.neko.night.utils.client.FileUtils.CONFIG_DIR
import cute.neko.night.utils.interfaces.Accessor
import java.io.File
import java.nio.file.Files

/**
 * @author yuchenxue
 * @date 2025/04/01
 */

object ConfigSystem : Accessor {

    fun save(name: String) {
        FileUtils.create()

        // total json
        val json = ConfigUtils.getConfig()

        // save
        try {
            Files.write(
                File(CONFIG_DIR, "$name.json").toPath(),
                GsonBuilder().setPrettyPrinting().create().toJson(json).toByteArray()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(name: String) {
        FileUtils.create()

        val file = File(CONFIG_DIR, "$name.json")

        if (!file.exists()) {
            return
        }

        try {
            val json = Gson().fromJson(Files.readString(file.toPath()), JsonObject::class.java)

            ConfigUtils.load(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
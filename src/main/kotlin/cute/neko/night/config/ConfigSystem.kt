package cute.neko.night.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import cute.neko.night.utils.client.FileUtils
import cute.neko.night.utils.client.FileUtils.CONFIG_DIR
import cute.neko.night.utils.client.FileUtils.gson
import cute.neko.night.utils.interfaces.Accessor
import java.io.File
import java.nio.file.Files

object ConfigSystem : Accessor {

    fun save(name: String): Boolean {
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
            return false
        }

        return true
    }

    fun load(name: String): Boolean {
        FileUtils.create()

        val file = CONFIG_DIR.resolve("$name.json")

        if (!file.exists()) {
            return false
        }

        try {
            val json = gson.fromJson(Files.readString(file.toPath()), JsonObject::class.java)

            ConfigUtils.load(json)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun list(): List<String> {
        return try {
            CONFIG_DIR
                .listFiles {
                    it.isFile && it.extension == "json"
                }
                .map { it.nameWithoutExtension.lowercase() }
                .toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun delete(name: String): Boolean {
        val file = CONFIG_DIR.resolve("$name.json")

        if (!file.exists()) {
            return false
        }

        try {
            Files.deleteIfExists(file.toPath())
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }
}
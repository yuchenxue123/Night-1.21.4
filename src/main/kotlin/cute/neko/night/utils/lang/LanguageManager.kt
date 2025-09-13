package cute.neko.night.utils.lang

import cute.neko.night.features.module.render.ModuleSettings
import cute.neko.night.utils.misc.resources.Resource
import java.util.concurrent.ConcurrentHashMap

fun translate(key: String): String = LanguageManager.translate(key)

object LanguageManager {

    private val map = ConcurrentHashMap<String, HashMap<String, String>>()

    val languages = arrayOf(
        "english",
        "chinese"
    )

    fun load() {
        languages.forEach { language ->
            val json = Resource.language("$language.json").toJson()
            val translates = HashMap<String, String>()
            json.asMap().forEach { (k, v) ->
                translates[k] = v.asString
            }
            map[language] = translates
        }
    }

    fun translate(key: String): String {
        val language = ModuleSettings.language

        val map = map[language] ?: return key

        if (!map.containsKey(key)) {
            return key
        }

        return map[key]!!
    }

    fun hasTranslate(key: String): Boolean {
        val language = ModuleSettings.language

        val map = map[language] ?: return false

        return map.containsKey(key)
    }
}
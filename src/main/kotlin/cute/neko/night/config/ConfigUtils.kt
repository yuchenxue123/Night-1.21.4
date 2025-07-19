package cute.neko.night.config

import com.google.gson.JsonObject
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleManager
import cute.neko.night.features.setting.type.mode.AbstractModeSetting
import cute.neko.night.features.setting.type.mode.MultiEnumSetting
import cute.neko.night.features.setting.type.number.FloatSetting
import cute.neko.night.features.setting.type.number.IntegerSetting
import cute.neko.night.features.setting.type.primitive.BooleanSetting
import cute.neko.night.utils.client.FileUtils.gson
import cute.neko.night.utils.client.KeyboardUtils

/**
 * @author yuchenxue
 * @date 2025/06/02
 */

object ConfigUtils {

    /**
     * 获取完整配置Json
     */
    fun getConfig(): JsonObject {
        val json = JsonObject()

        // modules json
        val modulesJson = JsonObject()
        ModuleManager.modules.forEach { m ->
            modulesJson.add(m.name, getModuleConfig(m))
        }
        json.add("modules", modulesJson)

        return json
    }

    /**
     * 加载配置
     */
    fun load(json: JsonObject) {
        if (json.has("modules")) {
            val modulesJson = json.getAsJsonObject("modules")

            ModuleManager.modules.forEach { m ->
                if (modulesJson.has(m.name)) {
                    val moduleJson = modulesJson.getAsJsonObject(m.name)
                    loadModuleConfig(m, moduleJson)
                }
            }
        }
    }

    /**
     * 获取模块的配置Json
     */
    fun getModuleConfig(module: ClientModule): JsonObject {
        val json = JsonObject()

        // state
        json.addProperty("state", module.state)

        // key
        val keyJson = JsonObject()
        keyJson.addProperty("name", KeyboardUtils.getKeyName(module.key))
        keyJson.addProperty("value", module.key)
        json.add("key", keyJson)

        // hide
        json.addProperty("hide", module.hidden)

        // settings
        val settingsJson = JsonObject()
        module.settings.forEach { setting ->
            when (setting) {
                is BooleanSetting -> settingsJson.addProperty(setting.name, setting.value)
                is IntegerSetting -> settingsJson.addProperty(setting.name, setting.value)
                is FloatSetting -> settingsJson.addProperty(setting.name, setting.value)

                is AbstractModeSetting -> {
                    val modeJson = JsonObject()
                    modeJson.add("modes", gson.toJsonTree(setting.modes))
                    modeJson.addProperty("value", setting.getAsString())

                    settingsJson.add(setting.name, modeJson)
                }

                is MultiEnumSetting -> {
                    val multiJson = JsonObject()
                    multiJson.add("selects", gson.toJsonTree(setting.selects))
                    multiJson.add("actives", gson.toJsonTree(setting.getActivesArray()))

                    settingsJson.add(setting.name, multiJson)
                }
            }
        }
        json.add("settings", settingsJson)

        return json
    }

    /**
     * 加载模块配置
     */
    fun loadModuleConfig(module: ClientModule, json: JsonObject) {
        // state
        if (json.has("state")) {
            module.state = json.get("state").asBoolean
        }

        // key
        if (json.has("key")) {
            val keyJson = json.getAsJsonObject("key")
            if (keyJson.has("value")) {
                module.key = keyJson.get("value").asInt
            }
        }

        // hidden
        if (json.has("hide")) {
            module.hidden = json.get("hide").asBoolean
        }

        // settings
        if (json.has("settings")) {
            val settingsJson = json.getAsJsonObject("settings")

            module.settings.forEach { setting ->
                if (!settingsJson.has(setting.name)) {
                    return@forEach
                }

                when (setting) {
                    is BooleanSetting -> setting.value = settingsJson.get(setting.name).asBoolean
                    is IntegerSetting -> setting.value = settingsJson.get(setting.name).asInt
                    is FloatSetting -> setting.value = settingsJson.get(setting.name).asFloat

                    is AbstractModeSetting -> {
                        val modeJson = settingsJson.getAsJsonObject(setting.name)
                        if (modeJson.has("value")) {
                            setting.setByString(modeJson.get("value").asString)
                        }
                    }

                    is MultiEnumSetting -> {
                        val multiJson = settingsJson.getAsJsonObject(setting.name)
                        if (multiJson.has("actives")) {
                            val actives = multiJson.get("actives").asJsonArray
                            actives.forEach {
                                setting.active(it.asString)
                            }
                        }
                    }
                }
            }
        }
    }
}
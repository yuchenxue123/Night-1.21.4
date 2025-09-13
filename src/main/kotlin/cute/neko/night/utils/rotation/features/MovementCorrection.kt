package cute.neko.night.utils.rotation.features

import cute.neko.night.features.setting.type.mode.SubMode

enum class MovementCorrection(override val modeName: String) : SubMode {

    /**
     * No movement correction,
     * also keep movement yaw
     */
    NONE("None"),

    /**
     * Correction movement by change yaw,
     * but you movement yaw will follow this yaw.
     */
    STRICT("Strict"),

    /**
     * Correction movement,
     * also keep movement yaw
     */
    SILENT("Silent"),

    /**
     * Direct change look
     */
    LOOK("Look")

}
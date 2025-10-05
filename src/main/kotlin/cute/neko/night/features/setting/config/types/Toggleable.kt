package cute.neko.night.features.setting.config.types

interface Toggleable {

    /**
     * 在开关状态变化时调用
     *
     * @param state 新开关状态
     * @return 返回新的开关状态
     */
    fun onToggled(state: Boolean): Boolean {
        if (state) {
            enable()
        } else {
            disable()
        }

        return state
    }

    /**
     * 在打开时调用
     */
    fun enable() {}

    /**
     * 在关闭时调用
     */
    fun disable() {}
}
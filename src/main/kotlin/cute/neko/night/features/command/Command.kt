package cute.neko.night.features.command

import cute.neko.night.utils.misc.debug.Debug

abstract class Command(
    val name: String,
    val alias: Array<String> = arrayOf(),
    val usage: String = ".$name"
) {
    protected val debug = Debug.create(Debug.Type.COMMAND)

    abstract fun execute(args: Array<String>)

    protected fun chatUsage() {
        Debug.create(Debug.Type.CLIENT).debug(usage)
    }
}
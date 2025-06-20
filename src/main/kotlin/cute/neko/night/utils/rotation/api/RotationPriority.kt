package cute.neko.night.utils.rotation.api

import cute.neko.night.event.EventListener
import cute.neko.night.utils.client.Priority

/**
 * @author yuchenxue
 * @date 2025/05/31
 */

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RotationPriority(val priority: Priority)

val EventListener.rotationPriority: Priority
    get() = this.javaClass.getAnnotation(RotationPriority::class.java)?.priority ?: Priority.ROTATION_NORMAL

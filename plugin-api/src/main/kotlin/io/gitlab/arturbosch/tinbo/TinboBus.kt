package io.gitlab.arturbosch.tinbo

import com.google.common.eventbus.EventBus

/**
 * @author Artur Bosch
 */
object TinboBus {
	internal val eventBus = EventBus()
}

fun Any.registerForEventBus() {
	TinboBus.eventBus.register(this)
}

fun <T> publish(event: T) {
	TinboBus.eventBus.post(event)
}
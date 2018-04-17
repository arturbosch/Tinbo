package io.gitlab.arturbosch.tinbo.api

import com.google.common.eventbus.EventBus
import io.gitlab.arturbosch.tinbo.api.plugins.PluginSupport

/**
 * @author Artur Bosch
 */
object TinboBus {

	private val eventBus = EventBus()

	@PluginSupport
	fun register(any: Any) {
		eventBus.register(any)
	}

	@PluginSupport
	fun unregister(any: Any) {
		eventBus.unregister(any)
	}

	@PluginSupport
	fun <T> publish(event: T) {
		eventBus.post(event)
	}
}

package io.gitlab.arturbosch.tinbo.config

import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
interface TinboMode {
	val id: String
	val editAllowed: Boolean get() = false
	val isSummarizable: Boolean get() = false

	companion object {
		val START = object : TinboMode {
			override val id: String = "tinbo"
			override val editAllowed: Boolean = false
		}
	}
}

interface ModeListener {
	fun change(mode: TinboMode)
}

@Component
object ModeManager {

	private val listeners: MutableList<ModeListener> = mutableListOf()

	var current: TinboMode = TinboMode.START
		set(value) {
			field = value
			listeners.forEach { it.change(value) }
		}
	var isBackCommandBlocked = false
		set(value) = synchronized(this) { field = value }

	fun isEditAllowed() = current.editAllowed
	fun isCurrentMode(mode: TinboMode) = mode == current
	fun register(listener: ModeListener) = listeners.add(listener)
	fun isSumAllowed(): Boolean = current.isSummarizable
}

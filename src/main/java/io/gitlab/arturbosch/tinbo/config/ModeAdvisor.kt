package io.gitlab.arturbosch.tinbo.config

import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
object ModeAdvisor {

	private var backBlocked = false

	fun isBackBlocked() = backBlocked

	fun setBackBlocked(value: Boolean) {
		synchronized(this) {
			backBlocked = value
		}
	}

	private var mode = Mode.START

	fun getMode() = mode

	fun setTimerMode() {
		mode = Mode.TIMER
	}

	fun isTimerMode(): Boolean {
		return mode == Mode.TIMER
	}

	fun setStartMode() {
		mode = Mode.START
	}

	fun isStartMode(): Boolean {
		return mode == Mode.START
	}

	fun setNotesMode() {
		mode = Mode.NOTES
	}

	fun isNotesMode(): Boolean {
		return mode == Mode.NOTES
	}

	fun setTasksMode() {
		mode = Mode.TASKS
	}

	fun isTasksMode(): Boolean {
		return mode == Mode.TASKS
	}

	fun setFinanceMode() {
		mode = Mode.FINANCE
	}

	fun isFinanceMode(): Boolean {
		return mode == Mode.FINANCE
	}

	fun isModeWhereEditIsAllowed(): Boolean {
		return isFinanceMode() || isNotesMode() || isTasksMode() || isTimerMode()
	}
}

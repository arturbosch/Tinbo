package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.config.Notification
import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import io.gitlab.arturbosch.tinbo.api.model.AbstractExecutor
import io.gitlab.arturbosch.tinbo.api.orValue
import io.gitlab.arturbosch.tinbo.api.utils.printInfo
import io.gitlab.arturbosch.tinbo.api.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author artur
 */
@Component
open class TimeExecutor @Autowired constructor(timeDataHolder: TimeDataHolder,
											   private val terminal: TinboTerminal,
											   private val config: TinboConfig) :
		AbstractExecutor<TimeEntry, TimeData, DummyTime>(timeDataHolder, config) {

	override val tableHeader: String
		get() = "No.;Category;Date;HH:MM:SS;Notice"

	override fun newEntry(index: Int, dummy: DummyTime): TimeEntry {
		val entry = entriesInMemory[index]
		return entry.copy(dummy.category, dummy.message, dummy.hours,
				dummy.minutes?.validateInHourRange(),
				dummy.seconds?.validateInHourRange(), dummy.date)
	}

	private var currentTimer = Timer.INVALID
	private var running = false

	fun inProgress(): Boolean {
		return !currentTimer.isInvalid()
	}

	fun startPrintingTime(timer: Timer) {
		currentTimer = timer
		running = true
		while (running) {
			if (currentTimer.timerMode == TimerMode.DEFAULT)
				printInfo("\r${currentTimer.toTimeString()}")
			if (currentTimer.isFinished())
				stop()
			if (currentTimer.isPauseTime(config.getTimeInterval()))
				notify("Info")
			Thread.sleep(1000L)
		}
	}

	fun stop(name: String = "", message: String = "") {
		if (inProgress()) {
			running = false
			changeCategoryAndMessageIfNotEmpty(name, message)
			val category = if (currentTimer.category.isEmpty()) {
				terminal.readLine("Enter a category name: ").orValue(config.getCategoryName())
			} else currentTimer.category
			val description = if (currentTimer.message.isEmpty()) {
				terminal.readLine("Enter a description: ").orValue("")
			} else currentTimer.message
			currentTimer = currentTimer.copy(category = category, message = description)
			saveAndResetCurrentTimer()
		} else {
			printlnInfo("There is no current timer to stop.")
		}
	}

	private fun changeCategoryAndMessageIfNotEmpty(name: String, message: String) {
		if (name.isEmpty() && message.isEmpty()) return
		var newName = currentTimer.category
		var newMessage = currentTimer.message
		if (name.isNotEmpty()) newName = name
		if (message.isNotEmpty()) newMessage = message
		currentTimer = currentTimer.copy(category = newName, message = newMessage)
	}

	private fun saveAndResetCurrentTimer() {
		notify("Finished")
		createNewTimeEntry()
		currentTimer = Timer.INVALID
	}

	private fun createNewTimeEntry() {
		val (hours, mins, secs) = currentTimer.getTimeTriple() - currentTimer.getPauseTriple()
		addEntry(TimeEntry(currentTimer.category, currentTimer.message, hours, mins, secs,
				currentTimer.startDateTime.toLocalDate()))
	}

	private fun notify(headMessage: String) {
		Notification.notify(headMessage, currentTimer.toTimeString())
	}

	fun showTimer(): String {
		return if (inProgress()) currentTimer.toTimeString()
		else "No current timer is running"
	}

	fun changeTimeMode(mode: TimerMode) {
		currentTimer = currentTimer.copy(timerMode = mode)
	}

	fun pauseTimer() {
		currentTimer.currentPauseTime = LocalDateTime.now()
	}

	fun stopPauseTimer() {
		if (isPause()) {
			currentTimer.pauseTimes.add(currentTimer.currentPauseTime!! to Timer.calcPause(currentTimer))
			currentTimer.currentPauseTime = null
		}
	}

	fun isPause(): Boolean {
		return currentTimer.currentPauseTime != null
	}

}

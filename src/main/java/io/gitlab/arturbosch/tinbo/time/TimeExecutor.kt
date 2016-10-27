package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.TiNBo
import io.gitlab.arturbosch.tinbo.model.AbstractExecutor
import io.gitlab.arturbosch.tinbo.config.Notification
import io.gitlab.arturbosch.tinbo.orValue
import io.gitlab.arturbosch.tinbo.utils.printInfo
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import jline.console.ConsoleReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author artur
 */
@Component
open class TimeExecutor @Autowired constructor(val timeDataHolder: TimeDataHolder,
											   val consoleReader: ConsoleReader) :
		AbstractExecutor<TimeEntry, TimeData, DummyTime>(timeDataHolder) {

	override val TABLE_HEADER: String
		get() = "No.;Category;Date;HH:MM:SS;Notice"

	val SUMMARY_HEADER = "No.;Category;Spent"

	override fun newEntry(index: Int, dummy: DummyTime): TimeEntry {
		val entry = entriesInMemory[index]
		return entry.copy(dummy.category, dummy.message, dummy.hours, dummy.minutes, dummy.seconds, dummy.date)
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
			if (currentTimer.timeMode == TimeMode.DEFAULT)
				printInfo("\r${currentTimer.toTimeString()}")
			if (currentTimer.isFinished())
				stop()
			if (currentTimer.isPauseTime(TiNBo.config.getTimeInterval()))
				notify("Info")
			Thread.sleep(1000L)
		}
	}

	fun stop(name: String = "", message: String = "") {
		if (inProgress()) {
			running = false
			changeCategoryAndMessageIfNotEmpty(name, message)
			if (currentTimer.category.isEmpty()) {
				val category = consoleReader.readLine("Enter a category name: ").orValue(TiNBo.config.getCategoryName())
				val description = consoleReader.readLine("Enter a description: ").orValue("")
				currentTimer = currentTimer.copy(category = category, message = description)
			}
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
		if (inProgress()) return currentTimer.toTimeString()
		else return "No current timer is running"
	}

	fun changeTimeMode(mode: TimeMode) {
		currentTimer = currentTimer.copy(timeMode = mode)
	}

	fun sumAllCategories(): String {
		val summaries = timeDataHolder.createSummaries()
		return tableAsString(summaries, SUMMARY_HEADER)
	}

	fun sumForCategories(filters: List<String>): String {
		val summaries = timeDataHolder.createFilteredSummaries(filters)
		return tableAsString(summaries, SUMMARY_HEADER)
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

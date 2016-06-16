package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.TiNBo
import com.gitlab.artismarti.tinbo.common.AbstractExecutor
import com.gitlab.artismarti.tinbo.config.Notification
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.plusElementAtBeginning
import com.gitlab.artismarti.tinbo.utils.printInfo
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import com.gitlab.artismarti.tinbo.withIndexedColumn
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

	override fun newEntry(index: Int, dummy: DummyTime): TimeEntry {
		val realTime = entriesInMemory[index]
		return TimeEntry(
				dummy.category.orValue(realTime.category),
				dummy.message.orValue(realTime.message),
				dummy.hours.orValue(realTime.hours),
				dummy.minutes.orValue(realTime.minutes),
				dummy.seconds.orValue(realTime.seconds),
				dummy.date.orValue(realTime.date))
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
				printInfo("\r$timer")
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
				currentTimer = Timer(currentTimer.timeMode, category, description,
						currentTimer.startDateTime, currentTimer.stopDateTime,
						currentTimer.currentPauseTime, currentTimer.pauseTimes)
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
		currentTimer = Timer(currentTimer.timeMode, newName, newMessage,
				currentTimer.startDateTime, currentTimer.stopDateTime)
	}

	private fun saveAndResetCurrentTimer() {
		notify("Finished")
		createNewTimeEntry()
		currentTimer = Timer.INVALID
	}

	private fun createNewTimeEntry() {
		val (secs, mins, hours) = currentTimer.getTimeTriple() - currentTimer.getPauseTriple()
		addEntry(TimeEntry(currentTimer.category, currentTimer.message, hours, mins, secs,
				currentTimer.startDateTime.toLocalDate()))
	}

	private fun notify(headMessage: String) {
		Notification.notify(headMessage, currentTimer.toString())
	}

	fun showTimer(): String {
		if (inProgress()) return "Elapsed time: " + currentTimer.toString()
		else return "No current timer is running"
	}

	fun changeTimeMode(mode: TimeMode) {
		currentTimer = Timer(mode, currentTimer.category, currentTimer.message,
				currentTimer.startDateTime, currentTimer.stopDateTime)
	}

	fun sumAllCategories(): String {
		val summaries = timeDataHolder.createSummaries()
		return tableAsString(summaries)
	}

	private fun tableAsString(summaries: List<String>): String {
		return csv.asTable(
				summaries.withIndexedColumn()
						.plusElementAtBeginning("Nr.;Category;Spent")
		).joinToString(separator = "\n")
	}

	fun sumForCategories(categories: String): String {
		val filters = categories.split(Regex("[,;. ]+")).map { it.trim().toLowerCase() }
		val summaries = timeDataHolder.createFilteredSummaries(filters)
		return tableAsString(summaries)
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

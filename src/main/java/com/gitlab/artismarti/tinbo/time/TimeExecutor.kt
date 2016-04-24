package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.common.AbstractExecutor
import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.config.Notification
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.plusElementAtBeginning
import com.gitlab.artismarti.tinbo.utils.printInfo
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import com.gitlab.artismarti.tinbo.withIndexedColumn
import jline.console.ConsoleReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class TimeExecutor @Autowired constructor(val timeDataHolder: TimeDataHolder) :
		AbstractExecutor<TimeEntry, TimeData, DummyTime>(timeDataHolder) {

	override val TABLE_HEADER: String
		get() = "No.;Category;Date;Hr.;Min;Sec;Notice"

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

	init {
		timeDataHolder.loadData(Default.DATA_NAME)
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
				printInfo("\rElapsed time: $timer")
			if (currentTimer.isFinished())
				stop()
			if (currentTimer.isPauseTime(Default.INFO_NOTIFICATION_TIME))
				notify("Info")
			Thread.sleep(1000L)
		}
	}

	fun stop(name: String = "", message: String = "") {
		if (inProgress()) {
			running = false
			changeCategoryAndMessageIfNotEmpty(name, message)
			if (currentTimer.category.isEmpty()) {
				val consoleReader = ConsoleReader()
				val category = consoleReader.readLine("Enter a category name: ").orValue(Default.MAIN_CATEGORY_NAME)
				val description = consoleReader.readLine("Enter a description: ").orValue("")
				currentTimer = Timer(currentTimer.timeMode, category, description,
						currentTimer.startDateTime, currentTimer.stopDateTime)
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
		createTimeEntry()
		currentTimer = Timer.INVALID
	}

	private fun createTimeEntry() {
		val (secs, mins, hours) = currentTimer.getTimeTriple()
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
		val filters = categories.split(Regex(",")).map { it.trim() }
		val summaries = timeDataHolder.createFilteredSummaries(filters)
		return tableAsString(summaries)
	}

}


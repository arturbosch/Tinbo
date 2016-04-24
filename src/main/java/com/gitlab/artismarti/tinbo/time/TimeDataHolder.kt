package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.persistence.AbstractDataHolder
import com.gitlab.artismarti.tinbo.toNumberString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class TimeDataHolder @Autowired constructor(data: TimeData, persister: TimePersister) :
		AbstractDataHolder<TimeEntry, TimeData>(data, persister) {

	override fun newData(name: String, entriesInMemory: List<TimeEntry>): TimeData {
		return TimeData(name, entriesInMemory)
	}

	override fun getEntriesFilteredBy(filter: String): List<TimeEntry> {
		return getEntries().filter { it.category == filter }
	}

	fun createSummaries(): List<String> {
		return summariesInternal(getEntries())
	}

	private fun summariesInternal(entries: List<TimeEntry>): List<String> {
		val categoryToTimeString = entries.groupBy { it.category }.mapValues { sumTimesAsString(it.value) }
		return categoryToTimeString.map { it.key + ";" + it.value }
	}

	private fun sumTimesAsString(value: List<TimeEntry>): String {
		val seconds = value.map { it.seconds }.sum()
		val (extraMinutes, realSeconds) = divAndMod(seconds)
		val minutes = value.map { it.minutes }.sum().plus(extraMinutes)
		val (extraHours, realMinutes) = divAndMod(minutes)
		val hours = value.map { it.hours }.sum().plus(extraHours)

		return "${hours.toNumberString()}:${realMinutes.toNumberString()}:${realSeconds.toNumberString()}"
	}

	private fun divAndMod(value: Long) = value.div(60) to value.mod(60)

	fun createFilteredSummaries(by: List<String>): List<String> {
		val entries = getEntries().filter { by.contains(it.category) }
		return summariesInternal(entries)
	}
}
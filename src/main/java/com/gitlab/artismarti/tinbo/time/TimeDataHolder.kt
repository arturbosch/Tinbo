package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.TiNBo
import com.gitlab.artismarti.tinbo.common.AbstractDataHolder
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.toTimeString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TimeDataHolder @Autowired constructor(persister: TimePersister) :
		AbstractDataHolder<TimeEntry, TimeData>(persister) {

	override val last_used_data: String
		get() = TiNBo.config.getKey(ConfigDefaults.TIMERS)
				.getOrElse(ConfigDefaults.LAST_USED, { Defaults.TIME_NAME })


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

		return "${hours.toTimeString()}:${realMinutes.toTimeString()}:${realSeconds.toTimeString()}"
	}

	private fun divAndMod(value: Long) = value.div(60) to value.mod(60)

	fun createFilteredSummaries(by: List<String>): List<String> {
		val entries = getEntries().filter { by.contains(it.category.toLowerCase()) }
		return summariesInternal(entries)
	}

	override fun changeCategory(oldName: String, newName: String) {
		val updatedEntries = getEntries().map {
			if (it.category.equals(oldName, ignoreCase = true)) {
				TimeEntry(newName, it.message, it.hours, it.minutes, it.seconds, it.date)
			} else {
				it
			}
		}
		saveData(data.name, updatedEntries)
	}
}

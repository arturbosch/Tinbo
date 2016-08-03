package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.common.Entry
import io.gitlab.arturbosch.tinbo.spaceIfEmpty
import io.gitlab.arturbosch.tinbo.toTimeString
import io.gitlab.arturbosch.tinbo.utils.dateFormatter
import java.time.LocalDate

/**
 * @author artur
 */
class TimeEntry(var category: String = "Main",
				var message: String = "invalid",
				var hours: Long = -1L,
				var minutes: Long = -1L,
				var seconds: Long = -1L,
				var date: LocalDate = LocalDate.now()) : Entry() {

	override fun toString(): String {
		return "$category;${date.format(dateFormatter)};${hours.toTimeString()}:${minutes.toTimeString()}" +
				":${seconds.toTimeString()};${message.spaceIfEmpty()}"
	}

	override fun compareTo(other: Entry): Int {
		if (other !is TimeEntry) return -1
		return this.date.compareTo(other.date)
	}

	fun copy(category: String?, message: String?, hours: Long?, minutes: Long?,
			 seconds: Long?, date: LocalDate?): TimeEntry {
		return TimeEntry(category ?: this.category, message ?: this.message,
				hours ?: this.hours, minutes ?: this.minutes, seconds ?: this.seconds, date ?: this.date)
	}
}


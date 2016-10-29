package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.model.Entry
import io.gitlab.arturbosch.tinbo.spaceIfEmpty
import io.gitlab.arturbosch.tinbo.orSpace
import io.gitlab.arturbosch.tinbo.utils.dateTimeFormatter
import java.time.LocalDateTime
import java.util.Objects

/**
 * @author artur
 */
class TaskEntry(val message: String = "invalid",
				val description: String = "invalid",
				val location: String = "invalid",
				val category: String = "invalid",
				val startTime: LocalDateTime = LocalDateTime.now(),
				val endTime: LocalDateTime = LocalDateTime.now()) : Entry() {

	override fun toString(): String {
		return "${category.spaceIfEmpty()};${message.spaceIfEmpty()};${location.spaceIfEmpty()};" +
				"${startTime.format(dateTimeFormatter)};${endTime.format(dateTimeFormatter)};" +
				description.spaceIfEmpty()
	}

	override fun compareTo(other: Entry): Int {
		if (other !is TaskEntry) return 1
		return this.startTime.compareTo(other.startTime)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is TaskEntry) return false

		if (message != other.message) return false
		if (description != other.description) return false
		if (location != other.location) return false
		if (category != other.category) return false
		if (startTime != other.startTime) return false
		if (endTime != other.endTime) return false

		return true
	}

	override fun hashCode(): Int {
		return Objects.hash(message, description, location, category, startTime, endTime)
	}

	fun copy(message: String?, description: String?, location: String?, category: String?,
			 startTime: LocalDateTime?, endTime: LocalDateTime?): TaskEntry {
		return TaskEntry(message ?: this.message, description ?: this.description, location ?: this.location,
				category ?: this.category, startTime ?: this.startTime, endTime ?: this.endTime)
	}
}

package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.TiNBo
import io.gitlab.arturbosch.tinbo.commands.EditableCommands
import io.gitlab.arturbosch.tinbo.config.Defaults
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.nullIfEmpty
import io.gitlab.arturbosch.tinbo.orDefault
import io.gitlab.arturbosch.tinbo.toLongOrNull
import io.gitlab.arturbosch.tinbo.utils.DateTimeFormatters
import io.gitlab.arturbosch.tinbo.utils.dateFormatter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * @author artur
 */
@Component
open class TimeEditCommands @Autowired constructor(executor: TimeExecutor) :
		EditableCommands<TimeEntry, TimeData, DummyTime>(executor) {

	override val id: String = "time"

	private val SUCCESS_MESSAGE = "Successfully added a time entry."

	@CliAvailabilityIndicator("editTime", "newTime", "loadTime")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTimerMode()
	}

	override fun add(): String {
		return whileNotInEditMode {
			val category = console.readLine("Enter a category: ").orDefault(TiNBo.config.getCategoryName())
			val message = console.readLine("Enter a message: ")
			val date = console.readLine("Enter a date (yyyy-mm-dd), leave empty for today: ")
			try {
				val hours = readNumber("Enter amount of spent hours: ")
				val mins = readNumber("Enter amount of spent minutes: ")
				val seconds = readNumber("Enter amount of spent seconds: ")
				addTime(hours, mins, seconds, category, message, date)
			} catch(e: NumberFormatException) {
				"Could not parse given time values. Use numbers only!"
			}
		}
	}

	private fun readNumber(message: String) = console.readLine(message).orDefault("0").toLong()

	@CliCommand("newTime", "addTime", help = "Adds a new time entry without executing a timer.")
	fun addTime(@CliOption(key = arrayOf("hours", "h"),
			specifiedDefaultValue = "0",
			unspecifiedDefaultValue = "0",
			help = "Duration in hours.") hours: Long,
				@CliOption(key = arrayOf("minutes", "m", "mins"),
						specifiedDefaultValue = "0",
						unspecifiedDefaultValue = "0",
						help = "Duration in minutes.") mins: Long,
				@CliOption(key = arrayOf("seconds", "s", "mins"),
						specifiedDefaultValue = "0",
						unspecifiedDefaultValue = "0",
						help = "Duration in seconds.") seconds: Long,
				@CliOption(key = arrayOf("category", "cat", "c"),
						unspecifiedDefaultValue = Defaults.MAIN_CATEGORY_NAME,
						specifiedDefaultValue = Defaults.MAIN_CATEGORY_NAME,
						help = "Category of the time entry.") name: String,
				@CliOption(key = arrayOf("message", "msg"),
						unspecifiedDefaultValue = "",
						specifiedDefaultValue = "",
						help = "Note for this tracking.", mandatory = true) message: String,
				@CliOption(key = arrayOf("date"),
						help = "Specify a date for this time entry. Format: yyyy-MM-dd",
						specifiedDefaultValue = "",
						unspecifiedDefaultValue = "") date: String): String {

		return whileNotInEditMode {
			try {
				val localDate = if (date.isEmpty()) LocalDate.now() else LocalDate.parse(date, dateFormatter)
				executor.addEntry(TimeEntry(name, message, hours, mins, seconds, localDate))
				SUCCESS_MESSAGE
			} catch(e: DateTimeParseException) {
				"Could not parse given date."
			}
		}
	}

	override fun edit(index: Int): String {
		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val category = console.readLine("Enter a category (empty if unchanged): ").nullIfEmpty()
				val message = console.readLine("Enter a message (empty if unchanged): ").nullIfEmpty()
				val dateFormat = console.readLine("Enter a date (yyyy-mm-dd) (empty if unchanged): ").orEmpty()
				try {
					val hours = console.readLine("Enter amount of spent hours (empty if unchanged): ").toLongOrNull()
					val mins = console.readLine("Enter amount of spent minutes (empty if unchanged): ").toLongOrNull()
					val seconds = console.readLine("Enter amount of spent seconds (empty if unchanged): ").toLongOrNull()
					val date: LocalDate? = DateTimeFormatters.parseDateOrNull(dateFormat)
					executor.editEntry(i, DummyTime(category, message, hours, mins, seconds, date))
					SUCCESS_MESSAGE
				} catch(e: NumberFormatException) {
					"Could not parse given time values. Use numbers only!"
				}

			}
		}
	}

	@CliCommand("editTime", help = "Edits a time entry.")
	fun editTime(@CliOption(key = arrayOf("", "i", "index"),
			mandatory = true,
			help = "Index of the task to edit.") index: Int,
				 @CliOption(key = arrayOf("hours", "h"),
						 help = "Duration in hours.") hours: Long?,
				 @CliOption(key = arrayOf("minutes", "m", "mins"),
						 help = "Duration in minutes.") mins: Long?,
				 @CliOption(key = arrayOf("seconds", "s", "mins"),
						 help = "Duration in seconds.") seconds: Long?,
				 @CliOption(key = arrayOf("category", "cat", "c"),
						 help = "Category of the time entry.") name: String?,
				 @CliOption(key = arrayOf("message", "msg"),
						 help = "Note for this tracking.") message: String?,
				 @CliOption(key = arrayOf("date"), specifiedDefaultValue = "", unspecifiedDefaultValue = "",
						 help = "Specify a date for this time entry. Format: yyyy-MM-dd") dateFormat: String): String {

		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val date: LocalDate? = DateTimeFormatters.parseDateOrNull(dateFormat)
				executor.editEntry(i, DummyTime(name, message, hours, mins, seconds, date))
				SUCCESS_MESSAGE
			}
		}
	}

}


package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.TiNBo
import com.gitlab.artismarti.tinbo.common.Command
import com.gitlab.artismarti.tinbo.common.EditableCommands
import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.orDefault
import com.gitlab.artismarti.tinbo.toLongOrNull
import com.gitlab.artismarti.tinbo.utils.DateTimeFormatters
import com.gitlab.artismarti.tinbo.utils.dateFormatter
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
		EditableCommands<TimeEntry, TimeData, DummyTime>(executor), Command {

	override val id: String = "time"

	private val SUCCESS_MESSAGE = "Successfully added a time entry."

	@CliAvailabilityIndicator("editTime", "newTime", "loadTime")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTimerMode()
	}

	@CliCommand("loadTime", help = "Changes the complete data set.")
	fun loadData(@CliOption(key = arrayOf("name"),
			help = "name of the data set to load",
			unspecifiedDefaultValue = Defaults.TIME_NAME,
			specifiedDefaultValue = Defaults.TIME_NAME) name: String) {
		executor.loadData(name)
	}

	override fun add(): String {
		return whileNotInEditMode {
			val category = console.readLine("Enter a category: ").orDefault(TiNBo.config.getCategoryName())
			val message = console.readLine("Enter a message: ")
			val date = console.readLine("Enter a date (yyyy-mm-dd), leave empty for today: ")
			try {
				val hours = console.readLine("Enter amount of spent hours: ").toLong()
				val mins = console.readLine("Enter amount of spent minutes: ").toLong()
				val seconds = console.readLine("Enter amount of spent seconds: ").toLong()
				addTime(hours, mins, seconds, category, message, date)
			} catch(e: NumberFormatException) {
				"Could not parse given time values. Use numbers only!"
			}
		}
	}

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
				val category = console.readLine("Enter a category (empty if unchanged): ")
				val message = console.readLine("Enter a message (empty if unchanged): ")
				val dateFormat = console.readLine("Enter a date (yyyy-mm-dd) (empty if unchanged): ")
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


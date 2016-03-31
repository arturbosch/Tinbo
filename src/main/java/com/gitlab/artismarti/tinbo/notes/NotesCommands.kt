package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.printer.printlnInfo
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * @author artur
 */
@Component
class NotesCommands(val executor: NotesExecutor = Injekt.get()) : CommandMarker {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @CliAvailabilityIndicator("note", "task", "load", "all")
    fun isAvailable(): Boolean {
        return ModeAdvisor.isNotesMode()
    }

    @CliCommand(value = "task", help = "Adds a new task.")
    fun newTask(@CliOption(key = arrayOf("message", "msg", "m"), mandatory = true, help = "Summary of the task.",
            specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
                @CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
                        specifiedDefaultValue = Default.MAIN_CATEGORY_NAME,
                        unspecifiedDefaultValue = Default.MAIN_CATEGORY_NAME) category: String,
                @CliOption(key = arrayOf("location", "loc", "l"), help = "Specify a location for this task.",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") location: String,
                @CliOption(key = arrayOf("description", "des", "d"), help = "Specify a description for this task.",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") description: String,
                @CliOption(key = arrayOf("start", "s"), help = "Specify a end time for this task. Format: yyyy-MM-dd HH:mm",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") startTime: String,
                @CliOption(key = arrayOf("end", "e"), help = "Specify a start time for this task. Format: yyyy-MM-dd HH:mm",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") endTime: String) {

        if (startTime.isEmpty() && endTime.isEmpty()) {
            executor.addNote(NoteEntry(message, description, location, category))
        } else if (startTime.isNotEmpty()) {
            try {
                val pair = parseDateTime(endTime, startTime)
                val formattedStartTime = pair.first
                var formattedEndTime = pair.second
                executor.addNote(NoteEntry(message, description, location, category, formattedStartTime, formattedEndTime))
            } catch(e: DateTimeParseException) {
                printlnInfo("Could not parse dates, use format: yyyy-MM-dd HH:mm")
            }
        }
    }

    private fun parseDateTime(endTime: String, startTime: String): Pair<LocalDateTime, LocalDateTime> {
        val formattedStartTime = LocalDateTime.parse(startTime, formatter)
        var formattedEndTime = formattedStartTime
        if (endTime.isNotEmpty()) formattedEndTime = LocalDateTime.parse(endTime, formatter)
        return Pair(formattedStartTime, formattedEndTime)
    }

    @CliCommand(value = "load", help = "Loads/Creates an other data set. Note data sets are stored under ~/tinbo/notes/*.")
    fun loadNotes(@CliOption(key = arrayOf("name", "n"), mandatory = true,
            specifiedDefaultValue = Default.NOTES_NAME,
            unspecifiedDefaultValue = Default.NOTES_NAME) name: String) {
        executor.loadData(name)
    }

    @CliCommand("note", "new", help = "Adds a new note.")
    fun newNote(@CliOption(key = arrayOf("message", "msg", "m"), mandatory = true, help = "Summary of the task.",
            specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
                @CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
                        specifiedDefaultValue = Default.MAIN_CATEGORY_NAME,
                        unspecifiedDefaultValue = Default.MAIN_CATEGORY_NAME) category: String): String {
        if (message.isEmpty()) return "You need to specify a message."
        else {
            executor.addNote(NoteEntry(message, "", "", category))
            return "Successfully added a note."
        }
    }

    @CliCommand("all", help = "Lists all notes and tasks.")
    fun listNotes(): String {
        return executor.listData()
    }

}

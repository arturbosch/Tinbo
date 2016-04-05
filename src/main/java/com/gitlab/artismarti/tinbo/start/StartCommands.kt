package com.gitlab.artismarti.tinbo.start

import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.providers.PromptProvider
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class StartCommands @Autowired constructor(val promptProvider: PromptProvider) : CommandMarker {

    @CliAvailabilityIndicator("time", "tasks", "notes")
    fun onlyModeCommands(): Boolean {
        return ModeAdvisor.isStartMode()
    }

    @CliAvailabilityIndicator("back")
    fun noExitCommand(): Boolean {
        return !ModeAdvisor.isStartMode()
    }

    @CliCommand("time", "timer", help = "Switch to time mode where you can start timers and list previous timings.")
    fun timerMode() {
        ModeAdvisor.setTimerMode()
        promptProvider.promptText = "time"
        printlnInfo("Entering time mode...")
    }

    @CliCommand("back", "b", help = "Exits current mode and enters start mode where you have access to all other modes.")
    fun startMode() {
        ModeAdvisor.setStartMode()
        promptProvider.promptText = "tinbo"
        printlnInfo("Entering tinbo mode...")
    }

    @CliCommand("tasks", help = "Switch to tasks mode to write down tasks.")
    fun tasksMode() {
        ModeAdvisor.setTasksMode()
        promptProvider.promptText = "tasks"
        printlnInfo("Entering tasks mode...")
    }

    @CliCommand("notes", help = "Switch to notes mode to write down tasks.")
    fun notesMode() {
        ModeAdvisor.setNotesMode()
        promptProvider.promptText = "notes"
        printlnInfo("Entering notes mode...")
    }
}

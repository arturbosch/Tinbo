package com.gitlab.artismarti.tinbo.start

import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.providers.ProviderHelper
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
class StartCommands @Autowired constructor(val providerHelper: ProviderHelper) : CommandMarker {

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
		providerHelper.changePrompt("time")
        printlnInfo("Entering time mode...")
    }

    @CliCommand("back", "b", help = "Exits current mode and enters start mode where you have access to all other modes.")
    fun startMode() {
        ModeAdvisor.setStartMode()
		providerHelper.changePrompt("tinbo")
        printlnInfo("Entering tinbo mode...")
    }

    @CliCommand("tasks", help = "Switch to tasks mode to write down tasks.")
    fun tasksMode() {
        ModeAdvisor.setTasksMode()
		providerHelper.changePrompt("tasks")
        printlnInfo("Entering tasks mode...")
    }

    @CliCommand("notes", help = "Switch to notes mode to write down tasks.")
    fun notesMode() {
        ModeAdvisor.setNotesMode()
		providerHelper.changePrompt("notes")
        printlnInfo("Entering notes mode...")
    }

    @CliCommand("welcome", help = "Shows the banner and welcome message.")
    fun welcome(): String {
		return "${providerHelper.getWelcome()}"
    }
}

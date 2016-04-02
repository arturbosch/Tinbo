package com.gitlab.artismarti.tinbo.start

import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import com.gitlab.artismarti.tinbo.providers.PromptProvider
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

    @CliAvailabilityIndicator("time", "notes")
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

    @CliCommand("notes", help = "Switch to notes mode to write down notes or tasks.")
    fun notesMode() {
        ModeAdvisor.setNotesMode()
        promptProvider.promptText = "notes"
        printlnInfo("Entering notes mode...")
    }
}

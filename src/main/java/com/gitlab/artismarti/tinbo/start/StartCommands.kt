package com.gitlab.artismarti.tinbo.start

import com.gitlab.artismarti.tinbo.printer.printlnInfo
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

    @CliAvailabilityIndicator("timer")
    fun onlyModeCommands(): Boolean {
        return ModeAdvicer.isStartMode()
    }

    @CliAvailabilityIndicator("back")
    fun noExitCommand(): Boolean {
        return !ModeAdvicer.isStartMode()
    }

    @CliCommand("timer", help = "Switch to timer mode where you can start timers and list previous timings.")
    fun timerMode() {
        ModeAdvicer.setTimerMode()
        promptProvider.promptText = "timer"
        printlnInfo("Entering timer mode...")
    }

    @CliCommand("back", "b", help = "Exits current mode and enters start mode where you have access to all other modes.")
    fun startMode() {
        ModeAdvicer.setStartMode()
        promptProvider.promptText = "tinbo"
        printlnInfo("Entering tinbo mode...")
    }
}

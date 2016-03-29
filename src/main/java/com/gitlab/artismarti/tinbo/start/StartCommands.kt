package com.gitlab.artismarti.tinbo.start

import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class StartCommands : CommandMarker {

    @CliAvailabilityIndicator("timer")
    fun onlyModeCommands(): Boolean {
        return ModeAdvicer.isStartMode()
    }

    @CliAvailabilityIndicator("exit")
    fun noExitCommand(): Boolean {
        return !ModeAdvicer.isStartMode()
    }

    @CliCommand("timer", help = "Switch to timer mode where you can start timers and list previous timings.")
    fun timerMode() {
        ModeAdvicer.setTimerMode()
        println("Timer: " + ModeAdvicer.isTimerMode())
    }

    @CliCommand("exit", help = "Exits current mode and enters start mode where you have access to all other modes.")
    fun startMode() {
        ModeAdvicer.setStartMode()
    }
}

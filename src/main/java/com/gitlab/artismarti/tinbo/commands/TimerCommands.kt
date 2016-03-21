package com.gitlab.artismarti.tinbo.commands

import com.gitlab.artismarti.tinbo.Notification
import org.fusesource.jansi.Ansi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.JLineShellComponent
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

/**
 * @author artur
 */
@Component
class TimerCommands @Autowired constructor(val shell: JLineShellComponent) : CommandMarker {

    private var currentTimer = Timer.INVALID
    private var running = false

    @CliCommand(value = "start", help = "Starts the timer and waits for you to type 'stop' to finish it if no arguments are specified. " +
            "Parameters '--minutes | --mins | --m' and '--seconds | --secs | --s' can be used to specify how long the timer should run.")
    fun startTimer(@CliOption(key = arrayOf("minutes", "m", "mins"), unspecifiedDefaultValue = "0", help = "Duration of timer in minutes.") mins: Int,
                   @CliOption(key = arrayOf("seconds", "s", "mins"), unspecifiedDefaultValue = "0", help = "Duration of timer in seconds.") seconds: Int) {

        if (currentTimer.isInvalid()) {
            running = true
            CompletableFuture.runAsync { startPrintingTime(Timer(mins, seconds)) }
        } else {
            shell.flash(Level.FINE, "Other timer already in process. Stop the timer before starting a new one.", "id")
        }
    }

    @CliCommand(value = "stop", help = "Stops the timer.")
    fun stopTimer() {
        internalStop()
    }

    @CliCommand(value = "q", help = "Stops the timer.")
    fun stopTimerWithQ() {
        internalStop()
    }

    private fun internalStop() {
        if (!currentTimer.isInvalid()) {
            running = false
            saveAndResetCurrentTimer()
        } else {
            shell.flash(Level.FINE, "Other timer already in process. Stop the timer before starting a new one.", "id")
        }
    }

    private fun saveAndResetCurrentTimer() {
        currentTimer = Timer.INVALID
        notify()
    }

    private fun notify() {
        Notification.info()
    }

    private fun startPrintingTime(timer: Timer) {
        currentTimer = timer
        while(running) {
            print(Ansi.ansi().fg(Ansi.Color.BLACK).bg(Ansi.Color.WHITE).a("\rElapsed time: $timer").reset())
            Thread.sleep(1000L)
        }
    }

    class Timer(val mins: Int = 0, val seconds: Int = 0, val startDateTime: LocalDateTime = LocalDateTime.now()) {

        override fun toString(): String {
            val now = LocalDateTime.now()
            val diffSecs = Duration.between(startDateTime, now).seconds.toNumberString()
            val diffMins = Duration.between(startDateTime, now).toMinutes().toNumberString()
            val diffHours = Duration.between(startDateTime, now).toHours().toNumberString()
            return "$diffHours:$diffMins:$diffSecs"
        }

        fun isInvalid(): Boolean {
            return this.equals(INVALID)
        }

        companion object {
            val INVALID = Timer(-1, -1)
        }
    }
}

fun Number.toNumberString(): String {
    return toString().apply { if (this.length == 1) return "0$this" }
}

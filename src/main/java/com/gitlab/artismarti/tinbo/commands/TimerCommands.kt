package com.gitlab.artismarti.tinbo.commands

import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

/**
 * @author artur
 */
@Component
class TimerCommands : CommandMarker {

    private val currentTimer = Timer.INVALID

    @CliCommand(value = "start", help = "Starts the timer and waits for you to type 'stop' to finish it if no arguments are specified. " +
            "Parameters '--minutes | --mins | --m' and '--seconds | --secs | --s' can be used to specify how long the timer should run.")
    fun startTimer(@CliOption(key = arrayOf("minutes", "m", "mins"), unspecifiedDefaultValue = "0", help = "Duration of timer in minutes.") mins: Int,
                   @CliOption(key = arrayOf("seconds", "s", "mins"), unspecifiedDefaultValue = "0", help = "Duration of timer in seconds.") seconds: Int) {

        if (currentTimer.isInvalid()) {
            println("Start with $mins mins and $seconds secs")
            startPrintingTime(Timer(mins, seconds))
        } else {
            println("Other timer already in process. Stop the timer before starting a new one.")
        }
    }

    private fun startPrintingTime(timer: Timer) {
        println("Elapsed time: $timer")
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

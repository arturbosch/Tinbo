package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.Notification
import com.gitlab.artismarti.tinbo.config.Default
import org.fusesource.jansi.Ansi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.JLineShellComponent
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

/**
 * Commands with that users can interact in timer mode.
 *
 * @author artur
 */
@Component
class TimerCommands @Autowired constructor(val shell: JLineShellComponent) : CommandMarker {

    private var currentTimer = Timer.INVALID
    private var running = false

    @Suppress("unused")
    @CliCommand(value = "start", help = "Starts the timer and waits for you to type 'stop' to finish it if no arguments are specified. " +
            "Parameters '--minutes | --mins | --m' and '--seconds | --secs | --s' can be used to specify how long the timer should run.")
    fun startTimer(@CliOption(key = arrayOf("minutes", "m", "mins"), unspecifiedDefaultValue = "0", help = "Duration of timer in minutes.") mins: Int,
                   @CliOption(key = arrayOf("seconds", "s", "mins"), unspecifiedDefaultValue = "0", help = "Duration of timer in seconds.") seconds: Int,
                   @CliOption(key = arrayOf("bg", "background"), unspecifiedDefaultValue = "false", specifiedDefaultValue = "true") bg: Boolean,
                   @CliOption(key = arrayOf("name", "n"), unspecifiedDefaultValue = Default.MAIN_CATEGORY_NAME,
                           specifiedDefaultValue = Default.MAIN_CATEGORY_NAME) name: String) {

        if (inputsAreInvalid(mins, seconds)) {
            shell.flash(Level.WARNING, "Invalid parameters: minutes and seconds have to be positiv and seconds not bigger than 59.\n", "inputs")
            return;
        }

        if (currentTimer.isInvalid()) {
            running = true
            val mode = specifyTimerMode(bg)
            CompletableFuture.runAsync {
                startPrintingTime(Timer(mode, name, stopDateTime = Timer.calcStopTime(mins, seconds)))
            }
        } else {
            shell.flash(Level.FINE, "Other timer already in process. Stop the timer before starting a new one.\n", "id")
        }
    }

    private fun specifyTimerMode(bg: Boolean): TimerMode {
        if (bg) return TimerMode.BACKGROUND
        else return TimerMode.DEFAULT
    }

    private fun inputsAreInvalid(mins: Int, seconds: Int): Boolean {
        return !(mins >= 0 && seconds >= 0 && seconds < 60)
    }

    @Suppress("unused")
    @CliCommand(value = "stop", help = "Stops the timer.")
    fun stopTimer() {
        internalStop()
    }

    @Suppress("unused")
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
        while (running) {
            if (currentTimer.timerMode == TimerMode.DEFAULT)
                printInfo("\rElapsed time: $timer")
            if (currentTimer.isFinished())
                internalStop()
            Thread.sleep(1000L)
        }
    }

    fun printInfo(message: String) {
        print(Ansi.ansi().fg(Ansi.Color.BLACK).bg(Ansi.Color.WHITE).a(message).reset())
    }


}


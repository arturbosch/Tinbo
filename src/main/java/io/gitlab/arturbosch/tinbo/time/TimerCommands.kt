package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.common.Command
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

/**
 * Commands users can write to interact in timer mode.
 *
 * @author artur
 */
@Component
open class TimerCommands @Autowired constructor(val executor: TimeExecutor) : Command {

	override val id: String = "time"

	@CliAvailabilityIndicator("show", "start", "stop", "q", "pause")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTimerMode()
	}

	@CliAvailabilityIndicator("bg", "fg")
	fun isTimerRunning(): Boolean {
		return ModeAdvisor.isTimerMode() && executor.inProgress()
	}

	@CliCommand(value = "start", help = "Starts the timer and waits for you to type 'stop' to finish it if no arguments are specified.")
	fun startTimer(@CliOption(key = arrayOf("minutes", "m", "mins"), specifiedDefaultValue = "0",
			unspecifiedDefaultValue = "0", help = "Duration of timer in minutes.") mins: Int,
	               @CliOption(key = arrayOf("seconds", "s", "mins"), specifiedDefaultValue = "0",
			               unspecifiedDefaultValue = "0", help = "Duration of timer in seconds.") seconds: Int,
	               @CliOption(key = arrayOf("background", "bg"), unspecifiedDefaultValue = "false",
			               specifiedDefaultValue = "true", help = "If the timer should be started in background.") bg: Boolean,
	               @CliOption(key = arrayOf("category", "cat", "c"), unspecifiedDefaultValue = "",
			               specifiedDefaultValue = "", help = "Category in which the time should be saved.") name: String,
	               @CliOption(key = arrayOf("message", "msg"), unspecifiedDefaultValue = "",
			               specifiedDefaultValue = "", help = "Note for this tracking.") message: String) {

		if (inputsAreInvalid(mins, seconds)) {
			printlnInfo("Invalid parameters: minutes and seconds have to be positive and seconds not bigger than 59.")
			return
		}

		if (!executor.inProgress()) {
			val mode = specifyTimerMode(bg)
			CompletableFuture.runAsync {
				executor.startPrintingTime(Timer(mode, name, message, stopDateTime = Timer.calcStopTime(mins, seconds)))
			}
		} else {
			printlnInfo("Other timer already in process. Stop the timer before starting a new one.")
		}
	}

	private fun specifyTimerMode(bg: Boolean): TimeMode {
		if (bg) return TimeMode.BACKGROUND
		else return TimeMode.DEFAULT
	}

	private fun inputsAreInvalid(mins: Int, seconds: Int): Boolean {
		return !(mins >= 0 && seconds >= 0 && seconds < 60)
	}

	@CliCommand(value = "bg", help = "Sends current timer to background")
	fun sendToBackground() {
		sendToMode(true)
	}

	@CliCommand(value = "fg", help = "Sends current timer to foreground")
	fun sendToForeground() {
		sendToMode(false)
	}

	private fun sendToMode(bg: Boolean) {
		if (executor.inProgress()) {
			executor.changeTimeMode(specifyTimerMode(bg))
		}
	}

	@CliCommand(value = "stop", help = "Stops the timer.")
	fun stopTimer(@CliOption(key = arrayOf("name", "n"),
			unspecifiedDefaultValue = "",
			specifiedDefaultValue = "",
			help = "Category in which the time should be saved.") name: String,
	              @CliOption(key = arrayOf("message", "msg"),
			              unspecifiedDefaultValue = "",
			              specifiedDefaultValue = "",
			              help = "Note for this tracking.") message: String) {
		executor.stop(name, message)
	}

	@CliCommand(value = "q", help = "Stops the timer.")
	fun stopTimerWithQ() {
		executor.stop()
	}

	@CliCommand(value = "show", help = "Shows the current running timer. Useful when in background mode.")
	fun showCurrentTimer(): String {
		return executor.showTimer()
	}

	@CliCommand("p", "pause", help = "Starts/Stops the pause timer.")
	fun startPause() {
		if (executor.inProgress()) {
			if (executor.isPause()) {
				executor.stopPauseTimer()
			} else {
				executor.pauseTimer()
			}
		} else println("No timer in progress!")
	}

}

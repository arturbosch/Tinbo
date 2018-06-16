package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.config.HomeFolder
import io.gitlab.arturbosch.tinbo.api.utils.dateTimeFormatter
import io.gitlab.arturbosch.tinbo.api.utils.printlnInfo
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

/**
 * @author Artur Bosch
 */
object ExitedTimers {

	private const val TIMER_FILE = ".timer"

	fun find(): Timer? {
		val timerFile = HomeFolder.get().resolve(TIMER_FILE)

		if (Files.exists(timerFile)) {
			val content = String(Files.readAllBytes(timerFile))
			try {
				val startTime = LocalDateTime.from(dateTimeFormatter.parse(content))
				return Timer(TimerMode.DEFAULT, "", "", startTime)
			} catch (_: DateTimeParseException) {
				printlnInfo("Could not parse unexpected exited timer file '$content'.")
			}
		} else {
			printlnInfo("No unexpected exited timer found.")
		}
		return null
	}

	fun exists(): Boolean {
		val timerFile = HomeFolder.get().resolve(TIMER_FILE)
		return Files.exists(timerFile)
	}

	fun init(timer: Timer) {
		val timerFile = HomeFolder.getFileResolved(TIMER_FILE)
		Files.write(timerFile, timer.startDateTime.format(dateTimeFormatter).toByteArray())
	}

	fun clear() {
		val timerFile = HomeFolder.get().resolve(TIMER_FILE)
		Files.deleteIfExists(timerFile)
	}
}

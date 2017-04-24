package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.orValue
import io.gitlab.arturbosch.tinbo.toTimeString
import java.time.Duration
import java.time.LocalDateTime
import java.util.ArrayList

/**
 * Domain model for timers. Specifies by a mode, start and end time.
 * Methods are provided to test if the timer is invalid or finished.
 * A convenient static calc end time method is provided.
 *
 * @author artur
 */
class Timer(val timerMode: TimerMode = TimerMode.INVALID,
			val category: String,
			val message: String,
			val startDateTime: LocalDateTime = LocalDateTime.now(),
			val stopDateTime: LocalDateTime? = null,
			var currentPauseTime: LocalDateTime? = null,
			val pauseTimes: MutableList<Pair<LocalDateTime, Long>> = ArrayList()) {

	companion object {

		val INVALID = Timer(TimerMode.INVALID, "INVALID", "INVALID")

		fun calcStopTime(mins: Int, seconds: Int): LocalDateTime? {
			var stop: LocalDateTime? = null
			if (mins >= 0 && seconds > 0) {
				stop = LocalDateTime.now()
						.plusMinutes(mins.toLong())
						.plusSeconds(seconds.toLong())
			}
			return stop
		}

		fun calcPause(timer: Timer): Long {
			val endPause = LocalDateTime.now()
			return Duration.between(timer.currentPauseTime ?: endPause, endPause).seconds
		}
	}

	fun toTimeString(): String {
		val (diffHours, diffMins, diffSecs) = getTimeTriple()
		val (pauseHours, pauseMins, pauseSecs) = getPauseTriple()

		val timeString =
				"Elapsed time: ${diffHours.toTimeString()}:${diffMins.toTimeString()}:${diffSecs.toTimeString()} (${category.orValue("Main")})"

		val returnString = when {
			pauseSecs == 0L && pauseMins == 0L && pauseHours == 0L -> timeString
			else -> timeString + " with pause time: ${pauseHours.toTimeString()}:${pauseMins.toTimeString()}:${pauseSecs.toTimeString()} "
		}

		return returnString
	}

	fun getPauseTriple(): Triple<Long, Long, Long> {
		val pauseSeconds = pauseTimes.map { it.second }.sum()
		val diffSeconds = pauseSeconds.mod(60)
		val pauseMinutes = pauseSeconds.div(60)
		val diffMinutes = pauseMinutes.mod(60)
		val pauseHours = pauseMinutes.div(60)
		return Triple(pauseHours, diffMinutes, diffSeconds) + getTimeTriple(currentPauseTime ?: LocalDateTime.now())
	}

	fun getTimeTriple(time: LocalDateTime = startDateTime): Triple<Long, Long, Long> {
		val now = LocalDateTime.now()
		val diffSecs = Duration.between(time, now).seconds.mod(60)
		val diffMins = Duration.between(time, now).toMinutes().mod(60)
		val diffHours = Duration.between(time, now).toHours().mod(60)
		return Triple(diffHours, diffMins, diffSecs)
	}

	fun isInvalid(): Boolean {
		return this == INVALID
	}

	fun isFinished(): Boolean {
		if (stopDateTime == null) {
			return false
		}
		return LocalDateTime.now() >= stopDateTime
	}

	fun isPauseTime(mins: Long): Boolean {
		val now = LocalDateTime.now()
		val diffSecs = Duration.between(startDateTime, now).seconds.mod(60)
		val diffMins = Duration.between(startDateTime, now).toMinutes().mod(60)
		return diffMins.mod(mins) == 0L && diffSecs == 0L && diffMins + diffSecs != 0L
	}

	override fun toString(): String {
		return "Timer(timeMode=$timerMode, category='$category', message='$message', startDateTime=$startDateTime, stopDateTime=$stopDateTime, currentPauseTime=$currentPauseTime, pauseTimes=$pauseTimes)"
	}

}


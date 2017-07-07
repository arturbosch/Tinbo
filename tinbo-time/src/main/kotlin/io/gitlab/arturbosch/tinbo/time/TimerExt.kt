package io.gitlab.arturbosch.tinbo.time

import java.time.LocalDateTime

/**
 * @author Artur Bosch
 */

fun Timer.copy(timerMode: TimerMode? = null,
			   category: String? = null,
			   message: String? = null,
			   startDateTime: LocalDateTime? = null,
			   stopDateTime: LocalDateTime? = null,
			   currentPauseTime: LocalDateTime? = null,
			   pauseTimes: MutableList<Pair<LocalDateTime, Long>>? = null): Timer {
	return Timer(timerMode ?: this.timerMode,
			category ?: this.category,
			message ?: this.message,
			startDateTime ?: this.startDateTime,
			stopDateTime ?: this.stopDateTime,
			currentPauseTime ?: this.currentPauseTime,
			pauseTimes ?: this.pauseTimes)
}

infix operator fun Triple<Long, Long, Long>.plus(other: Triple<Long, Long, Long>): Triple<Long, Long, Long> {
	val seconds = this.third + other.third
	val realSeconds = seconds.rem(60)
	val carryMinutes = seconds.div(60)
	val minutes = this.second + other.second + carryMinutes
	val realMinutes = minutes.rem(60)
	val carryHours = minutes.div(60)
	val hours = this.first + other.first + carryHours
	return Triple(hours, realMinutes, realSeconds)
}

infix operator fun Triple<Long, Long, Long>.minus(other: Triple<Long, Long, Long>): Triple<Long, Long, Long> {
	var carry = 0
	var secs = this.third - other.third
	if (secs < 0) {
		secs += 60
		carry = 1
	}
	var mins = this.second - other.second - carry
	if (mins < 0) {
		mins += 60
		carry = 1
	} else {
		carry = 0
	}
	val hours = this.first - other.first - carry
	if (hours < 0) throw IllegalStateException("Somehow the time calculation went wrong ... (times: $this and $other)")
	return Triple(hours, mins, secs)
}

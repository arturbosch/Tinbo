package io.gitlab.arturbosch.tinbo.time

import org.junit.Test
import java.time.LocalDateTime

/**
 * @author artur
 */
class TimerKtTest {

	@Test
	fun minusSecs() {
		val triple = Triple(0L, 1L, 1L)
		val triple2 = Triple(0L, 0L, 59L)

		val new = triple minus triple2

		assert(new.third == 2L && new.second == 0L)
	}

	@Test
	fun minusMins() {
		val triple = Triple(4L, 35L, 1L)
		val triple2 = Triple(0L, 42L, 59L)

		val new = triple minus triple2

		assert(new.third == 2L && new.second == 52L && new.first == 3L)
	}

	@Test
	fun minusHours() {
		val triple = Triple(4L, 35L, 1L)
		val triple2 = Triple(1L, 42L, 59L)

		val new = triple minus triple2

		assert(new.third == 2L && new.second == 52L && new.first == 2L)
	}

	@Test(expected = IllegalStateException::class)
	fun minusHoursFails() {
		val triple = Triple(4L, 35L, 1L)
		val triple2 = Triple(5L, 42L, 59L)

		triple minus triple2
	}

	@Test
	fun pauseStringOfTimerMustBeInValidTimeFormat() {
		val timer = Timer(TimerMode.DEFAULT, "Test", "", startDateTime = LocalDateTime.now().minusMinutes(188),
				currentPauseTime = LocalDateTime.now().minusHours(1).minusMinutes(58).minusSeconds(88))

		val timeString = timer.toTimeString()
		assert(timeString.substring(timeString.length - 9).split(":")
				.filter { it.trim().toLong().div(60) == 0L }.size == 3)
	}

}
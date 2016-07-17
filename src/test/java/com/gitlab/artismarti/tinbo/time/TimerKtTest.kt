package com.gitlab.artismarti.tinbo.time

import org.junit.Test

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

}
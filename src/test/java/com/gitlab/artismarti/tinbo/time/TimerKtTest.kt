package com.gitlab.artismarti.tinbo.time

import org.junit.Test

/**
 * @author artur
 */
class TimerKtTest {

	@Test
	fun minus() {
		val triple = Triple(0L, 1L, 1L)
		val triple2 = Triple(0L, 0L, 59L)

		val new = triple minus triple2
		assert(new.third == 2L && new.second == 0L)
	}

}
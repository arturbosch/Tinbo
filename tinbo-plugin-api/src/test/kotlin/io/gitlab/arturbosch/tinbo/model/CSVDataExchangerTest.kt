package io.gitlab.arturbosch.tinbo.model

import io.gitlab.arturbosch.tinbo.model.util.CSVDataExchange
import org.junit.Test

/**
 * @author artur
 */
class CSVDataExchangerTest {

	class TestEntry(val i: Int = 5, val s: String = "") : Entry() {
		override fun compareTo(other: Entry): Int = 1
	}

	@Test
	fun persistAndTransformToEntries() {
		val exchange = CSVDataExchange()

		// Timers
		val persist = exchange.toCSV(listOf(TestEntry(), TestEntry(), TestEntry()))
		val transform = exchange.fromCSV(TestEntry::class.java, persist)
		assert(transform.size == 3)

		// Notes
		val persist2 = exchange.toCSV(listOf(TestEntry(), TestEntry(), TestEntry()))
		val transform2 = exchange.fromCSV(TestEntry::class.java, persist2)
		assert(transform2.size == 3)
	}

}

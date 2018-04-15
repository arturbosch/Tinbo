package io.gitlab.arturbosch.tinbo.model

import io.gitlab.arturbosch.tinbo.api.model.util.CSVDataExchange
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.ArrayList
import kotlin.system.measureTimeMillis

/**
 * @author artur
 */
class CSVDataWriterIT {

	private var list = ArrayList<CSVDataExchangerTest.TestEntry>()
	private val writer = CSVDataExchange()

	@Before
	fun setUp() {
		for (i in 0..1000) {
			list.add(CSVDataExchangerTest.TestEntry())
		}
	}

	@After
	fun tearDown() {
		list.clear()
	}

	@Test
	fun persist() {
		val time1 = measureTimeMillis {
			testPersist1()
		}
		val time2 = measureTimeMillis {
			testPersist2()
		}

		println("Time1: " + time1)
		println("Time2: " + time2)
	}

	private fun testPersist1() {
		writer.toCSV(list)
	}

	private fun testPersist2() {
		writer.toCSV2(list)
	}

}

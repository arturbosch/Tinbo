package com.gitlab.artismarti.tinbo.persistence

import com.gitlab.artismarti.tinbo.timer.TimerEntry
import org.junit.Test

/**
 * @author artur
 */
class CSVDataExchangerTest {

    @Test
    fun persistAndTransformToEntries() {
        val exchange = CSVDataExchange()
        val persist = exchange.persist(listOf(TimerEntry(), TimerEntry(), TimerEntry()))
        val transform = exchange.transform(TimerEntry::class.java, persist)

        println(transform)
        assert(transform.size == 3)
    }
}

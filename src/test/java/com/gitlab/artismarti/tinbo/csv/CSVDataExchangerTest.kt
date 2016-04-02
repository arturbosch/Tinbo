package com.gitlab.artismarti.tinbo.csv

import com.gitlab.artismarti.tinbo.tasks.TaskEntry
import com.gitlab.artismarti.tinbo.timer.TimerEntry
import org.junit.Test

/**
 * @author artur
 */
class CSVDataExchangerTest {

    @Test
    fun persistAndTransformToEntries() {
        val exchange = CSVDataExchange()

        // Timers
        val persist = exchange.toCSV(listOf(TimerEntry(), TimerEntry(), TimerEntry()))
        val transform = exchange.fromCSV(TimerEntry::class.java, persist)
        println(transform)
        assert(transform.size == 3)

        // Notes
        val persist2 = exchange.toCSV(listOf(TaskEntry(), TaskEntry(), TaskEntry()))
        val transform2 = exchange.fromCSV(TaskEntry::class.java, persist2)
        println(transform2)
        assert(transform2.size == 3)
    }
}

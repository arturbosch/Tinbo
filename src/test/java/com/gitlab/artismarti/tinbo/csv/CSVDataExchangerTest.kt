package com.gitlab.artismarti.tinbo.csv

import com.gitlab.artismarti.tinbo.notes.NoteEntry
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
        val persist2 = exchange.toCSV(listOf(NoteEntry(), NoteEntry(), NoteEntry()))
        val transform2 = exchange.fromCSV(NoteEntry::class.java, persist2)
        println(transform2)
        assert(transform2.size == 3)
    }
}

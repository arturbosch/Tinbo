package com.gitlab.artismarti.tinbo.csv

import com.gitlab.artismarti.tinbo.csv.CSVDataExchange
import com.gitlab.artismarti.tinbo.timer.TimerEntry
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.ArrayList
import kotlin.system.measureTimeMillis

/**
 * @author artur
 */
class CSVDataWriterIT {

    private var list = ArrayList<TimerEntry>()
    private val writer = CSVDataExchange()

    @Before
    fun setUp() {
        for (i in 0..1000) {
            list.add(TimerEntry())
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
        writer.persist(list)
    }

    private fun testPersist2() {
        writer.persist2(list)
    }

}

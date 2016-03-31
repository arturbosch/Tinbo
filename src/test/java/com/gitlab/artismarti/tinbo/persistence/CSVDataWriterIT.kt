package com.gitlab.artismarti.tinbo.persistence

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

    //    val file = HomeFolder.getFile(HomeFolder.get().resolve(name))
    //    list.forEach {
    //        Files.write(file, (it + "\n").toByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    //    }
    //val file = HomeFolder.getFile(HomeFolder.get().resolve(name))
    //    Files.write(file, (line + "\n").toByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)

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

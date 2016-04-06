package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.HomeFolder
import org.junit.Test

/**
 * @author artur
 */
class TimerDomainTest {

    private val timerData = TimeData("Main")
    private val timerPersister = TimePersister(HomeFolder.getDirectory("test/timer"))
    private val timerDataHolder = TimeDataHolder(timerData, timerPersister)

    @Test
    fun domainObjectsTest() {
        val beforeSize = getEntriesSize()
        assert(beforeSize == 0)

        timerDataHolder.persistEntry(TimeEntry("TE1"))
        val afterPersistSize = getEntriesSize()
        assert(afterPersistSize == 1)

        timerDataHolder.persistEntry(TimeEntry("TE2"))
        val afterSecondPersistSize = getEntriesSize()
        assert(afterSecondPersistSize == 2)

        val isStored = timerPersister.store(timerData)
        assert(isStored)

        timerDataHolder.loadData(timerData.name)
        val totalEntrySizeAfterLoading = getEntriesSize()
        assert(totalEntrySizeAfterLoading == 2)

    }

    private fun getEntriesSize(): Int {
        return timerDataHolder.data.entries.size
    }

}

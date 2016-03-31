package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.HomeFolder
import org.junit.Test

/**
 * @author artur
 */
class TimerDomainTest {

    private val timerData = TimerData("Main")
    private val timerPersister = TimerPersister(HomeFolder.getDirectory("test/timer"))
    private val timerDataHolder = TimerDataHolder(timerData, timerPersister)

    @Test
    fun domainObjectsTest() {
        val beforeSize = getEntriesSize()
        assert(beforeSize == 0)

        timerDataHolder.persistEntry(TimerEntry("TE1"))
        val afterPersistSize = getEntriesSize()
        assert(afterPersistSize == 1)

        timerDataHolder.persistEntry(TimerEntry("TE2"))
        val afterSecondPersistSize = getEntriesSize()
        assert(afterSecondPersistSize == 2)

        val isStored = timerPersister.store(timerData)
        assert(isStored)

        val loadedData = timerPersister.restore(timerData.name)
        val totalEntrySizeAfterLoading = loadedData.entries.size
        assert(totalEntrySizeAfterLoading == 2)

    }

    private fun getEntriesSize(): Int {
        return timerDataHolder.data.entries.size
    }

}

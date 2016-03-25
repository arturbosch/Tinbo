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
        val beforeSize = getCategorySize(timerDataHolder)
        assert(beforeSize == 0)

        timerDataHolder.persistEntry("Category", TimerEntry("TE1"))
        val afterPersistSize = getCategorySize(timerDataHolder)
        assert(afterPersistSize == 1)

        timerDataHolder.persistEntry("Category", TimerEntry("TE2"))
        val afterSecondPersistSize = getCategorySize(timerDataHolder)
        assert(afterSecondPersistSize == 1)

        val totalEntrySize = getFirstCategoryEntriesSize(timerData)
        assert(totalEntrySize == 2)

        val isStored = timerPersister.store(timerData)
        assert(isStored)

        val loadedData = timerPersister.restore(timerData.name)
        val totalEntrySizeAfterLoading = getFirstCategoryEntriesSize(loadedData)
        assert(totalEntrySizeAfterLoading == 2)

    }

    private fun getFirstCategoryEntriesSize(data: TimerData) = data.categories[0].entries.size

    private fun getCategorySize(timerDataHolder: TimerDataHolder) = timerDataHolder.data.categories.size
}

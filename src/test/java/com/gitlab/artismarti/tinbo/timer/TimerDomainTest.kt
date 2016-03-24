package com.gitlab.artismarti.tinbo.timer

import org.junit.Test

/**
 * @author artur
 */
class TimerDomainTest {

    @Test
    fun domainObjectsTest() {
        val timerDataHolder = TimerDataHolder(TimerData("Main"), TimerPersister())
        val beforeSize = getCategorySize(timerDataHolder)

        timerDataHolder.persistEntry("Category", TimerEntry("TE1"))
        val afterPersistSize = getCategorySize(timerDataHolder)

        timerDataHolder.persistEntry("Category", TimerEntry("TE2"))
        val afterSecondPersistSize = getCategorySize(timerDataHolder)

        val totalEntrySize = timerDataHolder.data.getCategories()[0].getEntries().size

        assert(beforeSize == 0)
        assert(afterPersistSize == 1)
        assert(afterSecondPersistSize == 1)
        assert(totalEntrySize == 2)
    }

    private fun getCategorySize(timerDataHolder: TimerDataHolder) = timerDataHolder.data.getCategories().size
}

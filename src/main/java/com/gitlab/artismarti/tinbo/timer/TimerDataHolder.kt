package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.AbstractDataHolder
import com.gitlab.artismarti.tinbo.toSortedWithIndexedColumnStrings
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TimerDataHolder(data: TimerData = Injekt.get(), persister: TimerPersister = Injekt.get()) :
        AbstractDataHolder<TimerEntry, TimerData>(data, persister) {

    override fun newData(name: String, entriesInMemory: List<TimerEntry>): TimerData {
        return TimerData(name, entriesInMemory)
    }

    fun getEntriesFilteredByCategorySortedByDateAsString(categoryName: String): List<String> {
        return data.entries
                .filter { it.category == categoryName }
                .toSortedWithIndexedColumnStrings()
    }

    fun getEntriesSortedByDateAsString(): List<String> {
        return data.entries.toSortedWithIndexedColumnStrings()
    }
}

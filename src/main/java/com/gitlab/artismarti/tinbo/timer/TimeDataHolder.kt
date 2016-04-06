package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.AbstractDataHolder
import com.gitlab.artismarti.tinbo.toSortedWithIndexedColumnStrings
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TimeDataHolder(data: TimeData = Injekt.get(), persister: TimePersister = Injekt.get()) :
        AbstractDataHolder<TimeEntry, TimeData>(data, persister) {

    override fun newData(name: String, entriesInMemory: List<TimeEntry>): TimeData {
        return TimeData(name, entriesInMemory)
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

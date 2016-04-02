package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.Entry
import com.gitlab.artismarti.tinbo.toSortedWithIndexedColumnStrings
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TimerDataHolder(var data: TimerData = Injekt.get(),
                      val persister: TimerPersister = Injekt.get()) {

    fun loadData(name: String) {
        data = persister.restore(name) as TimerData
    }

    fun persistEntry(entry: Entry) {
        data.addEntry(entry)
        persister.store(data)
    }

    fun getEntriesFilteredByCategorySortedByDateAsString(categoryName: String): List<String> {
        return data.entries.map { it as TimerEntry }
                .filter { it.category == categoryName }
                .toSortedWithIndexedColumnStrings()
    }

    fun getEntriesSortedByDateAsString(): List<String> {
        return data.entries.toSortedWithIndexedColumnStrings()
    }
}

package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.AbstractDataHolder
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

    override fun getEntriesFilteredBy(filter: String): List<TimeEntry> {
        return data.entries.filter { it.category == filter }
    }
}

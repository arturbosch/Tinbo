package com.gitlab.artismarti.tinbo.timer

import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TimerDataHolder(var data: TimerData = Injekt.get(),
                      val persister: TimerPersister = Injekt.get()) {

    fun loadData(forCategory: String) {
        data = persister.restore(forCategory)
    }

    fun persistEntry(forCategory: String, entry: TimerEntry) {
        data.addForCategory(forCategory, entry)
        persister.store(data)
    }
}

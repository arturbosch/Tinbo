package com.gitlab.artismarti.tinbo.timer

import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TimerDataHolder(val data: TimerData = Injekt.get(),
                      val persister: TimerPersister = Injekt.get()) {

    fun persistEntry(forCategory: String, entry: TimerEntry) {
        data.addForCategory(forCategory, entry)
        persister.store(data)
    }
}

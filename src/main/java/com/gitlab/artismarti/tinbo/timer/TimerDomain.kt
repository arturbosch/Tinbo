package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.Category
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Entry

/**
 * @author artur
 */
class TimerEntry(var message: String = "invalid",
                 var hours: Int = -1,
                 var minutes: Int = -1,
                 var seconds: Int = -1) : Entry() {

    override fun toString(): String {
        return "TimerEntry(message='$message', hours=$hours, minutes=$minutes, seconds=$seconds)"
    }
}

/**
 * @author artur
 */
class TimerData(name: String = "",
                categories: List<Category> = listOf<TimerCategory>()) : Data(name, categories)

/**
 * @author artur
 */
class TimerCategory(name: String = "", entries: List<TimerEntry> = listOf<TimerEntry>()) : Category(name, entries)

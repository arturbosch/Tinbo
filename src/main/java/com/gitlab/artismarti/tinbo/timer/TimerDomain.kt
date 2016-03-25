package com.gitlab.artismarti.tinbo.timer

import com.fasterxml.jackson.annotation.JsonProperty
import com.gitlab.artismarti.tinbo.persistence.Category
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Entry

/**
 * @author artur
 */
class TimerEntry(@JsonProperty("message") var message: String = "invalid",
                 @JsonProperty("hours") var hours: Int = -1,
                 @JsonProperty("minutes") var minutes: Int = -1,
                 @JsonProperty("seconds") var seconds: Int = -1) : Entry() {

    override fun toString(): String {
        return "TimerEntry(message='$message', hours=$hours, minutes=$minutes, seconds=$seconds)"
    }
}

/**
 * @author artur
 */
class TimerData(name: String = "Data",
                categories: List<Category> = listOf<TimerCategory>()) : Data(name, categories)

/**
 * @author artur
 */
class TimerCategory(name: String = "Main", entries: List<TimerEntry> = listOf<TimerEntry>()) : Category(name, entries)

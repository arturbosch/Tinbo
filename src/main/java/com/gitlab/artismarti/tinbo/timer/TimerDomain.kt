package com.gitlab.artismarti.tinbo.timer

import com.fasterxml.jackson.annotation.JsonProperty
import com.gitlab.artismarti.tinbo.spaceIfEmpty
import com.gitlab.artismarti.tinbo.persistence.Category
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Entry
import java.time.LocalDate

/**
 * @author artur
 */
class TimerEntry(@JsonProperty("message") var message: String = "invalid",
                 @JsonProperty("hours") var hours: Long = -1L,
                 @JsonProperty("minutes") var minutes: Long = -1L,
                 @JsonProperty("seconds") var seconds: Long = -1L,
                 @JsonProperty("date") var date: LocalDate = LocalDate.now()) : Entry() {

    override fun toString(): String {
        return "$date;$hours;$minutes;$seconds;${message.spaceIfEmpty()}"
    }

    override fun compareTo(other: Entry): Int {
        if (other !is TimerEntry)
            return 1
        return this.date.compareTo(other.date)
    }
}

/**
 * @author artur
 */
class TimerData(name: String = "Data",
                categories: List<TimerCategory> = listOf<TimerCategory>()) : Data(name, categories)

/**
 * @author artur
 */
class TimerCategory(name: String = "Main", entries: List<TimerEntry> = listOf<TimerEntry>()) : Category(name, entries)

package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.Entry
import java.time.LocalDateTime
import java.util.Objects

/**
 * @author artur
 */
class NoteEntry(val message: String = "invalid",
                val description: String = "invalid",
                val location: String = "invalid",
                val category: String = "invalid",
                val startTime: LocalDateTime = LocalDateTime.now(),
                val endTime: LocalDateTime = LocalDateTime.now()) : Entry() {

    override fun toString(): String {
        return "$category;$message;$location;$startTime;$endTime;$description"
    }

    override fun compareTo(other: Entry): Int {
        throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NoteEntry) return false

        if (message != other.message) return false
        if (description != other.description) return false
        if (location != other.location) return false
        if (category != other.category) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(message, description, location, category, startTime, endTime)
    }
}

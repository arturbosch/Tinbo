package com.gitlab.artismarti.tinbo.persistence

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.gitlab.artismarti.tinbo.timer.TimerCategory

/**
 * @author artur
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = TimerCategory::class, name = "timerCategory")
)
abstract class Category(@JsonProperty("name") var name: String,
                        @JsonProperty("entries") var entries: List<Entry>) {

    fun addEntry(entry: Entry) {
        entries = entries.plus(entry)
    }

    override fun toString(): String {
        return "Category(name='$name', entries=$entries)"
    }

}

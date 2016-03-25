package com.gitlab.artismarti.tinbo.persistence

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.gitlab.artismarti.tinbo.timer.TimerCategory
import com.gitlab.artismarti.tinbo.timer.TimerData

/**
 * @author artur
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = TimerData::class, name = "timerData")
)
abstract class Data(@JsonProperty("name") var name: String,
                    @JsonProperty("categories") var categories: List<Category>) {

    private fun addCategory(category: Category) {
        categories = categories.plus(category)
    }

    fun addForCategory(categoryName: String, entry: Entry) {
        val category = categories.find { it.name == categoryName }
        category?.addEntry(entry) ?: addCategory(TimerCategory(categoryName).apply { addEntry(entry) })
    }

    override fun toString(): String {
        return "Data(name='$name', categories=$categories)"
    }

}

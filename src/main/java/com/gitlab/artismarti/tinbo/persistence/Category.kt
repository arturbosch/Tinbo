package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
abstract class Category(val name: String,
                        private var entries: List<Entry> = listOf<Entry>()) {

    fun getEntries(): List<Entry> {
        return entries
    }
    fun addEntry(entry: Entry) {
        entries = entries.plus(entry)
    }

    override fun toString(): String{
        return "Category(name='$name', entries=$entries)"
    }

}

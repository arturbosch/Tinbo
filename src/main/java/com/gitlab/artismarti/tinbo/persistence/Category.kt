package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
abstract class Category {
    val name = ""
    private var entries = listOf<Entry>()

    fun addEntry(entry: Entry) {
        entries = entries.plus(entry)
    }

    fun getEntries() = entries
}

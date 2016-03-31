package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
abstract class Data(var name: String,
                    var entries: List<Entry>) {

    fun addEntry(entry: Entry) {
        entries = entries.plus(entry)
    }

    override fun toString(): String {
        return "Data(name='$name', entries=$entries)"
    }

}

package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
interface DataHolder {

    fun loadData(name: String)

    fun persistEntry(entry: Entry)

    fun saveData(name: String, entriesInMemory: List<Entry>)

}

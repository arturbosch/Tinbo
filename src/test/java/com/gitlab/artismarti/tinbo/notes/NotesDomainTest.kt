package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.HomeFolder
import org.junit.Test

/**
 * @author artur
 */
class NotesDomainTest {

    private val notesData = NotesData("Main")
    private val notesPersister = NotesPersister(HomeFolder.getDirectory("test/notes"))
    private val notesDataHolder = NotesDataHolder(notesData, notesPersister)

    @Test
    fun domainObjectsTest() {
        val beforeSize = getEntriesSize()
        assert(beforeSize == 0)

        notesDataHolder.persistEntry(NoteEntry("note2"))
        val afterPersistSize = getEntriesSize()
        assert(afterPersistSize == 1)

        notesDataHolder.persistEntry(NoteEntry("note2"))
        val afterSecondPersistSize = getEntriesSize()
        assert(afterSecondPersistSize == 2)

        val isStored = notesPersister.store(notesData)
        assert(isStored)

        notesDataHolder.loadData(notesData.name)
        val totalEntrySizeAfterLoading = getEntriesSize()
        assert(totalEntrySizeAfterLoading == 2)

    }

    private fun getEntriesSize(): Int {
        return notesDataHolder.data.entries.size
    }

}

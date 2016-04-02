package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.HomeFolder
import org.junit.Test

/**
 * @author artur
 */
class NotesDomainTest {

    private val notesData = TaskData("Main")
    private val notesPersister = TaskPersister(HomeFolder.getDirectory("test/notes"))
    private val notesDataHolder = TaskDataHolder(notesData, notesPersister)

    @Test
    fun domainObjectsTest() {
        val beforeSize = getEntriesSize()
        assert(beforeSize == 0)

        notesDataHolder.persistEntry(TaskEntry("note2"))
        val afterPersistSize = getEntriesSize()
        assert(afterPersistSize == 1)

        notesDataHolder.persistEntry(TaskEntry("note2"))
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

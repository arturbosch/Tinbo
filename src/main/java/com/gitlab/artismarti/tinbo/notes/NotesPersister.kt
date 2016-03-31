package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.csv.CSVDataExchange
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Persister
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author artur
 */
class NotesPersister(private val NOTES_PATH: Path = HomeFolder.getDirectory("notes")) : Persister {

    private val writer = CSVDataExchange()

    override fun store(data: Data): Boolean {
        val persist = writer.toCSV(data.entries).joinToString("\n")
        val toSave = HomeFolder.getFile(NOTES_PATH.resolve(data.name))
        val saved = Files.write(toSave, persist.toByteArray())
        return Files.exists(saved)
    }

    override fun restore(name: String): Data {
        val path = NOTES_PATH.resolve(name)
        var data = NotesData(name)
        if (Files.exists(path)) {
            val entriesAsString = Files.readAllLines(path)
            val entries = writer.fromCSV(NoteEntry::class.java, entriesAsString)
            data.entries = entries
        }
        return data
    }

}

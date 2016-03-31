package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.csv.CSVDataExchange
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Persister
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author artur
 */
class TimerPersister(private val TIMER_PATH: Path = HomeFolder.getDirectory("timer")) : Persister {

    private val writer = CSVDataExchange()

    override fun store(data: Data): Boolean {
        val persist = writer.persist(data.entries).joinToString("\n")
        val toSave = HomeFolder.getFile(TIMER_PATH.resolve(data.name))
        val saved = Files.write(toSave, persist.toByteArray())
        return Files.exists(saved)
    }

    override fun restore(name: String): Data {
        val path = TIMER_PATH.resolve(name)
        var data = TimerData(name)
        if (Files.exists(path)) {
            val entriesAsString = Files.readAllLines(path)
            val entries = writer.transform(TimerEntry::class.java, entriesAsString)
            data.entries = entries
        }
        return data
    }
}

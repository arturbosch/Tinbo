package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.BasePersister
import com.gitlab.artismarti.tinbo.persistence.Data
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author artur
 */
class TimerPersister(private val TIMER_PATH: Path = HomeFolder.getDirectory("timer")) : BasePersister() {

    override fun store(data: Data): Boolean {
        val json = objectWriter.writeValueAsBytes(data)
        val toSave = HomeFolder.getFile(TIMER_PATH.resolve(data.name))
        val saved = Files.write(toSave, json)
        return Files.exists(saved)
    }

    override fun restore(name: String): TimerData {
        val path = TIMER_PATH.resolve(name)
        val toJson = Files.readAllLines(path).joinToString(separator = "")
        return objectMapper.readValue(toJson, TimerData::class.java)
    }
}

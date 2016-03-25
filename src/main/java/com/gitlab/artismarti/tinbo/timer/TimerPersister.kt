package com.gitlab.artismarti.tinbo.timer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.BasePersister
import com.gitlab.artismarti.tinbo.persistence.Data
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author artur
 */
class TimerPersister(private val TIMER_PATH: Path = HomeFolder.getDirectory("timer")) : BasePersister() {

    protected val objectMapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    protected val objectWriter: ObjectWriter = objectMapper.writerWithDefaultPrettyPrinter()

    override fun store(data: Data): Boolean {
        val json = objectWriter.writeValueAsBytes(data)
        val toSave = HomeFolder.getFile(TIMER_PATH.resolve(data.name))
        val saved = Files.write(toSave, json)
        return Files.exists(saved)
    }

    override fun restore(name: String): TimerData {
        val path = TIMER_PATH.resolve(name)
        var data = TimerData(name)
        if (Files.exists(path)) {
            val toJson = Files.readAllLines(path).joinToString(separator = "")
            data = objectMapper.readValue(toJson, TimerData::class.java)
        }
        return data
    }
}

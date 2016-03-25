package com.gitlab.artismarti.tinbo.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

/**
 * @author artur
 */
abstract class BasePersister : Persister {

    protected val objectMapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    protected val objectWriter: ObjectWriter = objectMapper.writerWithDefaultPrettyPrinter()

    abstract override fun store(data: Data): Boolean

    abstract override fun restore(name: String): Data
}

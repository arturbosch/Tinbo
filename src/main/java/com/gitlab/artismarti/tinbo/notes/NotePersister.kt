package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Persister
import java.nio.file.Path

/**
 * @author artur
 */
class NotePersister(private val NOTES_PATH: Path = HomeFolder.getDirectory("notes")) : Persister {

    override fun store(data: Data): Boolean {
        throw UnsupportedOperationException()
    }

    override fun restore(name: String): Data {
        throw UnsupportedOperationException()
    }

}

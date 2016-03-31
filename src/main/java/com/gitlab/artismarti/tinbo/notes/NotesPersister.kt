package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Persister

/**
 * @author artur
 */
class NotesPersister : Persister {

    override fun store(data: Data): Boolean {
        throw UnsupportedOperationException()
    }

    override fun restore(name: String): Data {
        throw UnsupportedOperationException()
    }

}

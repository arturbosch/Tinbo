package com.gitlab.artismarti.tinbo.persistence

import com.google.gson.Gson

/**
 * @author artur
 */
abstract class BasePersister : Persister {

    private val gson = Gson()

    override fun store(data: Data): Boolean {
        throw UnsupportedOperationException()
    }

    override fun restore(name: String): String {
        throw UnsupportedOperationException()
    }
}

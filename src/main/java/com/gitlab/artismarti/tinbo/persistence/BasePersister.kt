package com.gitlab.artismarti.tinbo.persistence

import com.google.gson.Gson

/**
 * @author artur
 */
abstract class BasePersister : Persister {

    private val gson = Gson()

    abstract override fun store(data: Data): Boolean

    abstract override fun restore(name: String): String
}

package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
abstract class BasePersister : Persister {

    abstract override fun store(data: Data): Boolean

    abstract override fun restore(name: String): Data
}

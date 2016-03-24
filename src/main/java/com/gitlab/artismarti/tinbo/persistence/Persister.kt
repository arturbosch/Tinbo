package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
interface Persister {

    fun store(data: Data): Boolean

    fun restore(name: String): String
}

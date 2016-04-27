package com.gitlab.artismarti.tinbo.common

/**
 * @author artur
 */
interface Editable {
	fun list(categoryName: String): String
	fun cancel(): String
	fun save(name: String): String
	fun delete(indexPattern: String): String
}

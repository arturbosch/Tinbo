package com.gitlab.artismarti.tinbo.common

/**
 * @author artur
 */
interface Editable {
	fun add(): String
	fun list(categoryName: String): String
	fun cancel(): String
	fun save(name: String): String
	fun delete(indexPattern: String): String
	fun changeCategory(oldName: String, newName: String): String
	fun data(): String
}

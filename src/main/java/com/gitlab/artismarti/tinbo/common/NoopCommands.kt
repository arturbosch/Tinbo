package com.gitlab.artismarti.tinbo.common

import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class NoopCommands : Editable {

	override fun add(): String = ""
	override fun list(categoryName: String): String = ""
	override fun cancel(): String = ""
	override fun save(name: String): String = ""
	override fun delete(indexPattern: String): String = ""
	override fun changeCategory(oldName: String, newName: String): String = ""

}

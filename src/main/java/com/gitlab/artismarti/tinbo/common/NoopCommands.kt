package com.gitlab.artismarti.tinbo.common

import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class NoopCommands : Editable {
	override fun list(categoryName: String): String {
		return ""
	}

	override fun cancel(): String {
		return ""
	}

	override fun save(name: String): String {
		return ""
	}

	override fun delete(indexPattern: String): String {
		return ""
	}

	override fun changeCategory(oldName: String, newName: String): String {
		return ""
	}
}

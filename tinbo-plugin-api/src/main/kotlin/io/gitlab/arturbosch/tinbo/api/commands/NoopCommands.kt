package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.marker.Editable
import io.gitlab.arturbosch.tinbo.api.marker.Summarizable
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
open class NoopCommands(override val id: String = "noop") : Editable, Summarizable, Command {
	override fun load(name: String): String = ""
	override fun edit(index: Int): String = ""
	override fun sum(categories: List<String>): String = ""
	override fun add(): String = ""
	override fun list(categoryName: String, all: Boolean): String = ""
	override fun cancel(): String = ""
	override fun save(name: String): String = ""
	override fun delete(indexPattern: String): String = ""
	override fun changeCategory(oldName: String, newName: String): String = ""
	override fun categories(): String = ""
	override fun data(): String = ""
}

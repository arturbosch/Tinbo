package io.gitlab.arturbosch.tinbo.lloc

import io.gitlab.arturbosch.loc.core.LOC
import io.gitlab.arturbosch.loc.languages.Language
import io.gitlab.arturbosch.loc.languages.LanguageStrategyFactory
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.plugins.TinboPlugin
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import java.io.File
import java.util.HashMap

/**
 * Implements a command to count logical lines of code for specified project path.
 *
 * @author Artur Bosch
 */
class LLOCPlugin : TinboPlugin(), Command {

	override fun version(): String = "1.0.1"
	override val id: String = "plugins"

	override fun registerCommands(tinboContext: TinboContext): List<Command> {
		return listOf(this)
	}

	private val languages = LanguageStrategyFactory.LANGUAGES.map { it.strategy }
	private val nullStrategy = Language.UNSUPPORTED.strategy

	@CliCommand("plugin lloc", help = "Runs lloc over specified project path and return the logical lines of code.")
	fun run(@CliOption(key = [""]) path: String?) = path?.let {

		val project = File(path)
		if (!project.exists()) return "Specified path does not exist!"

		val languageLocPairs = project.walkTopDown()
				.filter { it.isFile }
				.map { filePath ->
					val strategy = languages.find {
						it.isLangOfFileSame(filePath.toString())
					} ?: nullStrategy
					strategy.language.ending to LOC.count(strategy, filePath)
				}

		val languagePerLOC = languageLocPairs
				.groupByTo(HashMap(), { it.first }, { it.second })
				.map { it.key to it.value.sum() }

		val languageToLocString = languagePerLOC
				.filterNot { it.first == "NULL" }
				.filterNot { it.second == 0 }
				.joinToString(separator = "") { "\t${it.first} - ${it.second}\n" }

		"Project: $path:\n" + languageToLocString + "\nTotal LOC: ${languagePerLOC.sumBy { it.second }}"

	} ?: "You have to specify a project path to count LOC."
}

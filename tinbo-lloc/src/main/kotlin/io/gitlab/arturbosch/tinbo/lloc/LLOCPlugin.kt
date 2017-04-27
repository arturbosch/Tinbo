package io.gitlab.arturbosch.tinbo.lloc

import io.gitlab.arturbosch.loc.core.LOC
import io.gitlab.arturbosch.loc.languages.LanguageStrategyFactory
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import java.nio.file.Files
import java.nio.file.Paths
import java.util.HashMap
import java.util.stream.Collectors

/**
 * Implements a command to count logical lines of code for specified project path.
 *
 * @author Artur Bosch
 */
class LLOCPlugin : TiNBoPlugin {

	override fun registerCommands(tinboContext: SpringContext): List<Command> {
		return listOf(this)
	}

	private val languages = LanguageStrategyFactory.languages
			.map { LanguageStrategyFactory.getInstance(it) }
	private val nullStrategy = LanguageStrategyFactory.getInstance("")

	@CliCommand("plugin lloc", help = "Runs lloc over specified project path and return the logical lines of code.")
	fun run(@CliOption(key = arrayOf("")) path: String?): String {
		return path?.let {

			val project = Paths.get(path)
			if (Files.notExists(project)) return "Specified path does not exist!"

			val languageLocPairs = Files.walk(project)
					.filter { Files.isRegularFile(it) }
					.map { path ->
						val strategy = languages.find {
							it.isLangOfFileSame(path.toString())
						} ?: nullStrategy
						strategy.ending to LOC.count(strategy, path.toFile())
					}.collect(Collectors.toList<Pair<String, Int>>())

			val languagePerLOC = languageLocPairs
					.groupByTo(HashMap(), { it.first }, { it.second })
					.map { it.key to it.value.sum() }

			"Project: $path:\n" + languagePerLOC.filterNot { it.first == "null" }
					.joinToString(separator = "") { "\t${it.first} - ${it.second}\n" } +
					"\nTotal LOC: ${languagePerLOC.sumBy { it.second }}"

		} ?: "You have to specify a project path to count LOC."
	}
}
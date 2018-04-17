package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class ProjectsPlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun registerCommands(tinbo: TinboContext): List<Command> {
		val terminal = tinbo.beanOf<TinboTerminal>()
		val fileProjects = FileProjects()
		val csvTasks = CSVTasks()
		val currentProject = CurrentProject(csvTasks, fileProjects)
		tinbo.registerSingleton(currentProject)
		val projectCommands = ProjectCommands(terminal, currentProject)
		tinbo.registerSingleton(projectCommands)

		val csvProjects = CSVProjects(fileProjects)
		val pspCommands = PSPCommands(terminal, currentProject, csvProjects)
		tinbo.registerSingleton(pspCommands)

		val projectsModeCommand = StartProjectsModeCommand()
		tinbo.registerSingleton(projectsModeCommand)

		val projectsPluginSupport = ProjectsPluginSupport(fileProjects)
		tinbo.registerSingleton(projectsPluginSupport)
		return listOf(projectCommands, projectsModeCommand, pspCommands)
	}

}

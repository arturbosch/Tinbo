package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.EditablePromptProvider
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import jline.console.ConsoleReader
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class ProjectsPlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun registerCommands(tinboContext: TinboContext): List<Command> {
		val consoleReader = tinboContext.beanOf<ConsoleReader>()
		val fileProjects = FileProjects()
		val csvTasks = CSVTasks()
		val currentProject = CurrentProject(csvTasks, fileProjects)
		val projectCommands = ProjectCommands(consoleReader, currentProject)
		tinboContext.registerSingleton("ProjectCommands", projectCommands)

		val promptProvider = tinboContext.beanOf<EditablePromptProvider>()
		val csvProjects = CSVProjects(fileProjects)
		val pspCommands = PSPCommands(consoleReader, promptProvider, currentProject, csvProjects)
		tinboContext.registerSingleton("PSPCommands", pspCommands)

		val projectsModeCommand = StartProjectsModeCommand()
		tinboContext.registerSingleton("StartProjectsModeCommand", projectsModeCommand)

		val projectsPluginSupport = ProjectsPluginSupport(fileProjects)
		tinboContext.registerSingleton("ProjectsPluginSupport", projectsPluginSupport)
		return listOf(projectCommands, projectsModeCommand, pspCommands)
	}

}

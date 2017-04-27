package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.EditablePromptProvider
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import jline.console.ConsoleReader
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class ProjectsPlugin : TiNBoPlugin {

	override fun registerCommands(tinboContext: SpringContext): List<Command> {
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
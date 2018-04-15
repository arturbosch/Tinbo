package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.config.EditablePromptProvider
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Addable
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.marker.Listable
import io.gitlab.arturbosch.tinbo.api.model.Project
import io.gitlab.arturbosch.tinbo.api.utils.dateFormatter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * @author Artur Bosch
 */
@Component
class PSPCommands @Autowired constructor(private val console: TinboTerminal,
										 private val prompt: EditablePromptProvider,
										 private val currentProject: CurrentProject,
										 private val csvProjects: CSVProjects) : Command, Listable, Addable {

	override val id: String = "projects"

	override fun list(categoryName: String, all: Boolean): String {
		return if (categoryName.isNotEmpty()) showProject(categoryName) else csvProjects.convert()
	}

	override fun add(): String {
		return newProject(console.readLine("Enter project name: "))
	}

	@CliAvailabilityIndicator("showAll", "show-project", "new-project", "open-project", "close-project")
	fun available() = ModeManager.isCurrentMode(PSPMode) || ModeManager.isCurrentMode(ProjectsMode)

	@CliCommand("show-project", help = "Summarizes the specified project.")
	fun showProject(@CliOption(key = [""], help = "Name the project must start with.") name: String?): String {
		return name?.let { currentProject.showProject(it) } ?: "Specify a project by its name!"
	}

	@CliCommand("new-project", help = "Creates a new project.")
	fun newProject(@CliOption(key = [""], help = "Name the project must start with.") name: String?): String {
		val wrong = "Enter a valid and non existing name..."
		return name?.let {
			if (currentProject.projectWithNameExists(name)) return wrong
			return try {
				val description = console.readLine("Enter project description: ")
				val start = console.readLine("Enter start date of project (empty if today): ")
				val end = console.readLine("Enter end date of project(empty if not yet known): ")
				val startDate = if (start.isEmpty()) LocalDate.now() else LocalDate.parse(start, dateFormatter)
				val endDate = if (end.isEmpty()) null else LocalDate.parse(end, dateFormatter)
				currentProject.persistProject(Project(name, description, startDate, endDate))
				"Successfully added project $name"
			} catch (e: Exception) {
				e.message ?: throw e
			}
		} ?: wrong
	}

	@CliCommand("open-project", help = "Opens project with given name.")
	fun openProject(@CliOption(key = [""], help = "Name the project must start with.") name: String?): String {
		val wrong = "No such project, enter an existing name..."
		return name?.let {
			if (!currentProject.projectWithNameExists(name)) return wrong
			val realName = currentProject.enter(name)
			ModeManager.current = PSPMode
			prompt.promptText = realName
			return "Opening project $realName..."
		} ?: wrong
	}

	@CliCommand("close-project", help = "Sets the end date of a project.")
	fun closeProject(@CliOption(key = [""]) name: String?,
					 @CliOption(key = ["end"]) end: String?): String {

		try {

			if (name.isNullOrEmpty() && currentProject.isSpecified()) {
				val date = end?.let { LocalDate.parse(end, dateFormatter) } ?: LocalDate.now()
				currentProject.closeProject(currentProject.name(), date)
				return "Finished ${currentProject.name()}"
			}

			if (name.isNullOrEmpty()) return "Specify project name or open a project."

			val date = end?.let { LocalDate.parse(end, dateFormatter) } ?: LocalDate.now()
			currentProject.closeProject(name!!, date)
			ModeManager.current = ProjectsMode
			return "Closed $name project"

		} catch (e: DateTimeParseException) {
			return "Could not parse given date string!"
		}
	}

}

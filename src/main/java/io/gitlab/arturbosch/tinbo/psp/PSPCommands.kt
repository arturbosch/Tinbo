package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.Project
import io.gitlab.arturbosch.tinbo.Task
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.model.CSVAwareExecutor
import io.gitlab.arturbosch.tinbo.providers.PromptProvider
import io.gitlab.arturbosch.tinbo.utils.dateFormatter
import io.gitlab.arturbosch.tinbo.utils.lazyData
import jline.console.ConsoleReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

/**
 * @author Artur Bosch
 */
@Component
class PSPCommands @Autowired constructor(val console: ConsoleReader,
										 val prompt: PromptProvider,
										 val currentProject: CurrentProject,
										 val csvProjects: CSVProjects) : Command {
	override val id: String = "psp"

	@CliAvailabilityIndicator("showAll", "show-project", "new-project", "open-project")
	fun available() = ModeAdvisor.isProjectsMode()

	@CliCommand("showAll", help = "Shows all running projects.")
	fun projects(): String {
		return csvProjects.convert()
	}

	@CliCommand("show-project")
	fun showProject(@CliOption(key = arrayOf(""), help = "Name the project must start with.") name: String?): String {
		return name?.let { currentProject.showProject(it) } ?: "Specify a project by its name!"
	}

	@CliCommand("new-project", help = "Creates a new project.")
	fun newProject(@CliOption(key = arrayOf(""), help = "Name the project must start with.") name: String?): String {
		val wrong = "Enter a valid and non existing name..."
		return name?.let {
			if (currentProject.projectWithNameExists(name)) return wrong
			try {
				val description = console.readLine("Enter project description: ").orEmpty()
				val start = console.readLine("Enter start date of project (empty if today): ")
				val end = console.readLine("Enter end date of project(empty if not yet known): ")
				val startDate = if (start.isEmpty()) LocalDate.now() else LocalDate.parse(start, dateFormatter)
				val endDate = if (end.isEmpty()) null else LocalDate.parse(end, dateFormatter)
				currentProject.newProject(Project(name, description, startDate, endDate))
				return "Successfully added project $name"
			} catch(e: Exception) {
				return e.message ?: throw e
			}
		} ?: wrong
	}

	@CliCommand("open-project", help = "Opens project with given name.")
	fun openProject(@CliOption(key = arrayOf(""), help = "Name the project must start with.") name: String?): String {
		val wrong = "No such project, enter an existing name..."
		return name?.let {
			if (!currentProject.projectWithNameExists(name)) return wrong
			val realName = currentProject.enter(name)
			prompt.promptText = realName
			return "Opening project $realName..."
		} ?: wrong
	}

}

@Component
class CurrentProject @Autowired constructor(private val csvTasks: CSVTasks,
											private val fileProjects: FileProjects) {

	val unspecified = Project("undef")
	private var project: Project = unspecified
	private fun findByName(name: String) = fileProjects.projects.find {
		it.name.startsWith(name, ignoreCase = true)
	}

	fun enter(name: String): String {
		val maybeProject = findByName(name)
		project = maybeProject ?: unspecified
		return maybeProject?.name ?: "undef"
	}

	fun projectWithNameExists(name: String) = findByName(name) != null
	fun isSpecified(): Boolean = project != unspecified
	fun unspecify() {
		project = unspecified
	}

	fun name(): String = project.name
	fun addTask(task: Task) {
		if (isSpecified()) project.add(task)
	}

	fun showTasks(ofProject: Project = project): String = csvTasks.convert(ofProject.tasks)
	fun showProject(name: String): String {
		return findByName(name)?.let {
			"\n\nProject: ${it.name}\nDescription: ${it.description}\n" +
					"Project duration: ${it.start} - ${it.end}\n" +
					"Actual/planned time: ${it.sumActualTime()}/${it.sumPlannedTime()}\n" +
					"Actual/planned units: ${it.sumActualUnits()}/${it.sumPlannedUnits()}\n\n" +
					"Tasks:\n" + showTasks(it)
		} ?: "No project with given name $name found!"
	}

	fun newProject(project: Project) {
		fileProjects.persistProject(project)
	}

	fun closeTaskWithStartingName(name: String, minutes: Int, units: Int): Pair<Boolean, String> {
		val task = project.tasks.find { it.name.startsWith(name, ignoreCase = true) }
		return task?.let {
			it.complete(minutes, units)
			fileProjects.persistProject(project)
			true to it.name
		} ?: false to "undef"
	}

}

@Component
class FileProjects(val projectsPath: Path = HomeFolder.getDirectory("projects")) {

	private val yml = "yml"

	init {
		Assert.isTrue(Files.exists(projectsPath))
	}

	private var _projects: MutableList<Project> by lazyData {
		projectsPath.toFile().listFiles { file -> file.name.endsWith(yml) }
				.map { it.readText() }
				.map(String::fromYaml)
				.toMutableList()
	}

	val projects: List<Project>
		get() = _projects.toList()

	fun persistProject(project: Project) {
		val file = HomeFolder.getFile(projectsPath.resolve("${project.name}.$yml"))
		Files.write(file, project.toYaml().toByteArray())
		reloadIfNeeded(project)
	}

	private fun reloadIfNeeded(project: Project) {
		_projects.find { it.name == project.name }?.let {
			if (it != project) {
				_projects.remove(it)
				_projects.add(project)
			}
		} ?: _projects.add(project)
	}

}

@Component
class CSVProjects @Autowired constructor(private val fileProjects: FileProjects) : CSVAwareExecutor() {

	private val dummy = Project()
	override val TABLE_HEADER: String = "No.;" + dummy.csvHeader()

	fun convert(): String {
		val sums = fileProjects.projects.map { it.asCSV() }
		return tableAsString(sums)
	}

}

@Component
class CSVTasks : CSVAwareExecutor() {

	private val dummy = Task()
	override val TABLE_HEADER: String = "No.;" + dummy.csvHeader()

	fun convert(tasks: List<Task>): String {
		return tableAsString(tasks.map(Task::asCSV))
	}
}
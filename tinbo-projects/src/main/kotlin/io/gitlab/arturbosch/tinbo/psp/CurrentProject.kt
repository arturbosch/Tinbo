package io.gitlab.arturbosch.tinbo.psp

import com.google.common.eventbus.Subscribe
import io.gitlab.arturbosch.tinbo.Project
import io.gitlab.arturbosch.tinbo.Task
import io.gitlab.arturbosch.tinbo.asHourString
import io.gitlab.arturbosch.tinbo.registerForEventBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author Artur Bosch
 */
@Component
class CurrentProject @Autowired constructor(private val csvTasks: CSVTasks,
											private val fileProjects: FileProjects) {

	init {
		registerForEventBus()
	}

	@Subscribe
	fun handleUnspecifyEvent(event: UnspecifyProject) {
		unspecify()
	}

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
		if (isSpecified()) {
			project.add(task)
			persistProject(project)
		}
	}

	fun showTasks(ofProject: Project = project): String = csvTasks.convert(ofProject.tasks)

	fun showProject(name: String): String {
		return findByName(name)?.let {
			"\n\nProject: ${it.name}\nDescription: ${it.description}\n" +
					"Project duration: ${it.start} - ${it.end}\n" +
					"Actual/planned time: ${it.sumActualTime().asHourString()}/${it.sumPlannedTime().asHourString()}\n" +
					"Actual/planned units: ${it.sumActualUnits()}/${it.sumPlannedUnits()}\n\n" +
					"Tasks:\n" + showTasks(it)
		} ?: "No project with given name $name found!"
	}

	fun persistProject(theProject: Project) {
		fileProjects.persistProject(theProject)
	}

	fun closeTaskWithStartingName(name: String, minutes: Int, units: Int): Pair<Boolean, String> {
		val task = project.tasks.find { it.name.startsWith(name, ignoreCase = true) }
		return task?.let {
			it.complete(minutes, units)
			fileProjects.persistProject(project)
			true to it.name
		} ?: false to "undef"
	}

	fun closeProject(name: String, date: LocalDate) {
		findByName(name)?.let {
			it.end = date
			it.done = true
			fileProjects.persistProject(it)
		}
		unspecify()
	}

}

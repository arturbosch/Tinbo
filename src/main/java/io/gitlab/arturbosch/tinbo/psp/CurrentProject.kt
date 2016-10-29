package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.Project
import io.gitlab.arturbosch.tinbo.Task
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
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

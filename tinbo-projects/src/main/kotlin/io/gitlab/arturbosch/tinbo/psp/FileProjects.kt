package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.model.Project
import io.gitlab.arturbosch.tinbo.api.config.ConfigDefaults.PROJECTS
import io.gitlab.arturbosch.tinbo.api.config.HomeFolder
import io.gitlab.arturbosch.tinbo.api.model.util.lazyData
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
@Component
class FileProjects(private val projectsPath: Path = HomeFolder.getDirectory(PROJECTS)) {

	private val yml = "yml"

	init {
		Assert.isTrue(Files.exists(projectsPath), "The path to .quide/projects must exist!")
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

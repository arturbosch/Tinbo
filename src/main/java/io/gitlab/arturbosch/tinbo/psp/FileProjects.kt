package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.Project
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.utils.lazyData
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
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
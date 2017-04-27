package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.Project
import io.gitlab.arturbosch.tinbo.plugins.PluginHelper
import io.gitlab.arturbosch.tinbo.plugins.PluginSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class ProjectsPluginSupport @Autowired constructor(val fileProjects: FileProjects) : PluginHelper {

	@PluginSupport
	fun projects(): List<Project> = fileProjects.projects

}
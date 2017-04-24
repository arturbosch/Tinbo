package io.gitlab.arturbosch.tinbo.providers

import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.psp.CurrentProject
import io.gitlab.arturbosch.tinbo.psp.ProjectsMode
import org.springframework.stereotype.Component

/**
 * This class is used to evaluate necessary state changes when the mode is changed.
 *
 * @author Artur Bosch
 */
@Component
class StateProvider(val currentProject: CurrentProject) {

	fun isProjectOpen() = currentProject.isSpecified()

	fun unspecifyProjectIfNeeded(oldMode: TinboMode, newMode: TinboMode) {
		if (oldMode == ProjectsMode && newMode != ProjectsMode) {
			currentProject.unspecify()
		}
	}
}
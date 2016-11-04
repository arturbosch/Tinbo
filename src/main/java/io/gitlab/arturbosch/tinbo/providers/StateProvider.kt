package io.gitlab.arturbosch.tinbo.providers

import io.gitlab.arturbosch.tinbo.config.Mode
import io.gitlab.arturbosch.tinbo.psp.CurrentProject
import org.springframework.stereotype.Component

/**
 * This class is used to evaluate necessary state changes when the mode is changed.
 *
 * @author Artur Bosch
 */
@Component
class StateProvider(val currentProject: CurrentProject) {

	fun isProjectOpen() = currentProject.isSpecified()

	fun evaluate(oldMode: Mode, newMode: Mode) {
		if (oldMode == Mode.PROJECTS && newMode != Mode.PROJECTS) {
			currentProject.unspecify()
		}
	}
}
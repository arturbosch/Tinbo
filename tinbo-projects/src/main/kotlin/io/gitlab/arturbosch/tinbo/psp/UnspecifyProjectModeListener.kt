package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.config.ModeListener
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.publish

/**
 * @author Artur Bosch
 */
class UnspecifyProjectModeListener : ModeListener {

	override fun change(mode: TinboMode) {
		val old = ModeManager.current
		if (old == ProjectsMode && mode != ProjectsMode) {
			publish(UnspecifyProject())
		}
	}

}

class UnspecifyProject

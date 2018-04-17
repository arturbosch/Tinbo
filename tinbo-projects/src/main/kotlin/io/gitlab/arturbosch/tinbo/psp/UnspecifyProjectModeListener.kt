package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.TinboBus
import io.gitlab.arturbosch.tinbo.api.config.ModeListener
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode

/**
 * @author Artur Bosch
 */
class UnspecifyProjectModeListener : ModeListener {

	override fun change(mode: TinboMode) {
		val old = ModeManager.current
		if (old == ProjectsMode && mode != ProjectsMode) {
			TinboBus.publish(UnspecifyProject())
		}
	}
}

class UnspecifyProject

package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.api.config.ModeListener
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.publish
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class PromptChanger : ModeListener {
	override fun change(mode: TinboMode) {
		publish(PromptChangeEvent(mode.id))
	}
}

class PromptChangeEvent(val newName: String)

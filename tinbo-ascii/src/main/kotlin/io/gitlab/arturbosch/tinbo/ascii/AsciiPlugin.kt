package io.gitlab.arturbosch.tinbo.ascii

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class AsciiPlugin : TiNBoPlugin {

	override fun registerCommands(tinboContext: SpringContext): List<Command> {
		return listOf(Ascii())
	}

}
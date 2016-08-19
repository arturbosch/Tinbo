package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.providers.PromptProvider
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class BackCommand @Autowired constructor(val promptProvider: PromptProvider) : Command {

	override val id: String = "mode"

	@CliAvailabilityIndicator("back")
	fun noExitCommand(): Boolean {
		return !ModeAdvisor.isStartMode()
	}

	@CliCommand("back", "..", help = "Exits current mode and enters start mode where you have access to all other modes.")
	fun startMode() {
		ModeAdvisor.setStartMode()
		promptProvider.promptText = "tinbo"
		printlnInfo("Entering tinbo mode...")
	}
}

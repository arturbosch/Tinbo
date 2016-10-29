package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.Mode
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.providers.PromptProvider
import io.gitlab.arturbosch.tinbo.providers.StateProvider
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class BackCommand @Autowired constructor(val promptProvider: PromptProvider,
										 val stateProvider: StateProvider) : Command {

	override val id: String = "mode"

	@CliAvailabilityIndicator("back")
	fun noExitCommand(): Boolean {
		return !ModeAdvisor.isStartMode()
	}

	@CliCommand("back", "..", help = "Exits current mode and enters start mode where you have access to all other modes.")
	fun startMode() {
		if (ModeAdvisor.isBackBlocked()) {
			printlnInfo("Can't change mode when editing... use save or cancel.")
		} else {
			stateProvider.evaluate(ModeAdvisor.getMode(), Mode.START)
			promptProvider.promptText = "tinbo"
			ModeAdvisor.setStartMode()
			printlnInfo("Entering tinbo mode...")
		}
	}
}

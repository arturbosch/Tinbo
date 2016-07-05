package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.providers.PromptProvider
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class BackCommand @Autowired constructor(val promptProvider: PromptProvider) : Command {

	override val id: String = "share"

	@CliAvailabilityIndicator("back")
	fun noExitCommand(): Boolean {
		return !ModeAdvisor.isStartMode()
	}

	@CliCommand("back", "b", help = "Exits current mode and enters start mode where you have access to all other modes.")
	fun startMode() {
		ModeAdvisor.setStartMode()
		promptProvider.promptText = "tinbo"
		printlnInfo("Entering tinbo mode...")
	}
}

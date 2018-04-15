package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.utils.printlnInfo
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class StartFinanceModeCommand : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("finance")
	fun onlyModeCommands(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("finance", help = "Switch to finance mode where you can manage your finances.")
	fun financeMode() {
		ModeManager.current = FinanceMode
		printlnInfo("Entering finance mode...")
	}

}

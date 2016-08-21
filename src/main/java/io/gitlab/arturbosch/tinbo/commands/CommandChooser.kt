package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Editable
import io.gitlab.arturbosch.tinbo.api.Summarizable
import io.gitlab.arturbosch.tinbo.commands.NoopCommands
import io.gitlab.arturbosch.tinbo.config.Mode
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.finance.FinanceCommands
import io.gitlab.arturbosch.tinbo.notes.NoteCommands
import io.gitlab.arturbosch.tinbo.tasks.TaskCommands
import io.gitlab.arturbosch.tinbo.time.TimeEditCommands
import io.gitlab.arturbosch.tinbo.time.TimeSummaryCommands
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class CommandChooser @Autowired constructor(
		val timeEditCommands: TimeEditCommands,
		val timeSummaryCommands: TimeSummaryCommands,
		val noteCommands: NoteCommands,
		val taskCommands: TaskCommands,
		val financeCommands: FinanceCommands,
		val noopCommands: NoopCommands) {

	fun forCurrentMode(): Editable {
		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands
			Mode.TASKS -> taskCommands
			Mode.TIMER -> timeEditCommands
			Mode.FINANCE -> financeCommands
			else -> noopCommands
		}
	}

	fun forSummarizableMode(): Summarizable {
		return when (ModeAdvisor.getMode()) {
			Mode.TIMER -> timeSummaryCommands
			Mode.FINANCE -> financeCommands
			else -> noopCommands
		}
	}
}

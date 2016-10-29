package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Editable
import io.gitlab.arturbosch.tinbo.api.Listable
import io.gitlab.arturbosch.tinbo.api.Summarizable
import io.gitlab.arturbosch.tinbo.config.Mode
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.finance.FinanceCommands
import io.gitlab.arturbosch.tinbo.notes.NoteCommands
import io.gitlab.arturbosch.tinbo.providers.StateProvider
import io.gitlab.arturbosch.tinbo.psp.PSPCommands
import io.gitlab.arturbosch.tinbo.psp.ProjectCommands
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
		val pspCommands: PSPCommands,
		val projectCommands: ProjectCommands,
		val noopCommands: NoopCommands,
		val stateProvider: StateProvider) {

	fun forCurrentMode(): Editable {
		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands
			Mode.TASKS -> taskCommands
			Mode.TIMER -> timeEditCommands
			Mode.FINANCE -> financeCommands
			else -> noopCommands
		}
	}

	fun forListableMode(): Listable {
		return when (ModeAdvisor.getMode()) {
			Mode.PROJECTS -> if (stateProvider.isProjectOpen()) projectCommands else pspCommands
			else -> forCurrentMode()
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

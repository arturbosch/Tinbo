package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.config.Mode
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.notes.NoteCommands
import com.gitlab.artismarti.tinbo.tasks.TaskCommands
import com.gitlab.artismarti.tinbo.time.TimeEditCommands
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class CommandChooser @Autowired constructor(
		val timeEditCommands: TimeEditCommands,
		val noteCommands: NoteCommands,
		val taskCommands: TaskCommands,
		val noopCommands: NoopCommands) {

	fun forCurrentMode(): Editable {
		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands
			Mode.TASKS -> taskCommands
			Mode.TIMER -> timeEditCommands
			else -> noopCommands
		}
	}
}

package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.api.model.Data
import io.gitlab.arturbosch.tinbo.api.config.Defaults
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TaskData(name: String = Defaults.TASKS_NAME,
					entries: List<TaskEntry> = listOf()) : Data<TaskEntry>(name, entries)

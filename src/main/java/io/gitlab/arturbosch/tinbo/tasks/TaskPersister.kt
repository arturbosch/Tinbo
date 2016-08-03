package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.common.AbstractPersister
import io.gitlab.arturbosch.tinbo.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
@Component
open class TaskPersister(TASKS_PATH: Path = HomeFolder.getDirectory(ConfigDefaults.TASKS)) :
		AbstractPersister<TaskEntry, TaskData>(TASKS_PATH) {

	override fun restore(name: String): TaskData {
		return load(name, TaskData(name), TaskEntry::class.java)
	}

}

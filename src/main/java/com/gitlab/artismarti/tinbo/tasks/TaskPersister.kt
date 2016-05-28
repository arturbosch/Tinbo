package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.common.AbstractPersister
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.HomeFolder
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

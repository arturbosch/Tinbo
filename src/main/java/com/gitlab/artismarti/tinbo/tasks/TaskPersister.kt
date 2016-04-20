package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.AbstractPersister
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
@Component
class TaskPersister(TASKS_PATH: Path = HomeFolder.getDirectory("tasks")) :
		AbstractPersister<TaskEntry, TaskData>(TASKS_PATH) {

	override fun restore(name: String): TaskData {
		return save(name, TaskData(name), TaskEntry::class.java)
	}

}

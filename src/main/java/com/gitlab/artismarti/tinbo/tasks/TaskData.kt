package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.persistence.Data
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class TaskData(name: String = Default.TASKS_NAME,
			   entries: List<TaskEntry> = listOf()) : Data<TaskEntry>(name, entries)

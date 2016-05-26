package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.common.Data
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TaskData(name: String = Default.TASKS_NAME,
                    entries: List<TaskEntry> = listOf()) : Data<TaskEntry>(name, entries)

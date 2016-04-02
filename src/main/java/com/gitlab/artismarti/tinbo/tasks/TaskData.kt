package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Entry

/**
 * @author artur
 */
class TaskData(name: String = Default.TASKS_NAME, entries: List<Entry> = listOf()) : Data(name, entries)

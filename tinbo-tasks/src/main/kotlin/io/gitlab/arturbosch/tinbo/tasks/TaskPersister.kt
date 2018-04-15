package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.api.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.api.config.HomeFolder
import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import io.gitlab.arturbosch.tinbo.api.model.AbstractPersister
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TaskPersister @Autowired constructor(config: TinboConfig) :
		AbstractPersister<TaskEntry, TaskData>(HomeFolder.getDirectory(ConfigDefaults.TASKS), config) {

	override fun restore(name: String): TaskData {
		return load(name, TaskData(name), TaskEntry::class.java)
	}

}

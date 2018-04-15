package io.gitlab.arturbosch.tinbo.time

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
open class TimePersister @Autowired constructor(config: TinboConfig) :
		AbstractPersister<TimeEntry, TimeData>(HomeFolder.getDirectory(ConfigDefaults.TIMERS), config) {

	override fun restore(name: String): TimeData {
		return load(name, TimeData(name), TimeEntry::class.java)
	}
}

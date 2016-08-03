package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.common.AbstractPersister
import io.gitlab.arturbosch.tinbo.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
@Component
open class TimePersister(TIMER_PATH: Path = HomeFolder.getDirectory(ConfigDefaults.TIMERS)) :
		AbstractPersister<TimeEntry, TimeData>(TIMER_PATH) {

	override fun restore(name: String): TimeData {
		return load(name, TimeData(name), TimeEntry::class.java)
	}
}

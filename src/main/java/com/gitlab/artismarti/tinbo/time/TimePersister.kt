package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.common.AbstractPersister
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.HomeFolder
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

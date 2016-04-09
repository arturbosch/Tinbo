package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.AbstractPersister
import java.nio.file.Path

/**
 * @author artur
 */
class TimePersister(TIMER_PATH: Path = HomeFolder.getDirectory("timer")) : AbstractPersister<TimeEntry, TimeData>(TIMER_PATH) {

	override fun restore(name: String): TimeData {
		return save(name, TimeData(name), TimeEntry::class.java)
	}
}

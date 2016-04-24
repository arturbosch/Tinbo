package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.AbstractPersister
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
@Component
class TimePersister(TIMER_PATH: Path = HomeFolder.getDirectory("timer")) : AbstractPersister<TimeEntry, TimeData>(TIMER_PATH) {

	override fun restore(name: String): TimeData {
		return save(name, TimeData(name), TimeEntry::class.java)
	}
}

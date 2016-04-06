package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.AbstractPersister
import java.nio.file.Path

/**
 * @author artur
 */
class TimerPersister(TIMER_PATH: Path = HomeFolder.getDirectory("timer")) : AbstractPersister<TimerEntry, TimerData>(TIMER_PATH) {

    override fun restore(name: String): TimerData {
        return save(name, TimerData(name), TimerEntry::class.java)
    }
}

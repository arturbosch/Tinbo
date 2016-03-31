package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.persistence.Data


/**
 * @author artur
 */
class TimerData(name: String = Default.DATA_NAME, entries: List<TimerEntry> = listOf()) : Data(name, entries)

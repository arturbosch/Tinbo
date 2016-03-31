package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.Data


/**
 * @author artur
 */
class TimerData(name: String = "Data", entries: List<TimerEntry> = listOf()) : Data(name, entries)

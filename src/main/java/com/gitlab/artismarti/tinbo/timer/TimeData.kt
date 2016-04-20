package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.persistence.Data
import org.springframework.stereotype.Component


/**
 * @author artur
 */
@Component
class TimeData(name: String = Default.DATA_NAME,
			   entries: List<TimeEntry> = listOf()) : Data<TimeEntry>(name, entries)

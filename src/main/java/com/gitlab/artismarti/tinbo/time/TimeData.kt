package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.common.Data
import org.springframework.stereotype.Component


/**
 * @author artur
 */
@Component
open class TimeData(name: String = Default.DATA_NAME,
                    entries: List<TimeEntry> = listOf()) : Data<TimeEntry>(name, entries)

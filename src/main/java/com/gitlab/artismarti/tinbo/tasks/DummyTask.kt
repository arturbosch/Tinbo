package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.persistence.DummyEntry
import java.time.LocalDateTime

/**
 * @author artur
 */
class DummyTask(val message: String, val category: String, val location: String, val description: String,
                val startTime: LocalDateTime?, val endTime: LocalDateTime?) : DummyEntry()

package com.gitlab.artismarti.tinbo.tasks

import java.time.LocalDateTime

/**
 * @author artur
 */
class DummyTask(val message: String, val category: String, val location: String, val description: String,
                val startTime: LocalDateTime?, val endTime: LocalDateTime?)

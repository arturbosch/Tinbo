package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.common.DummyEntry
import java.time.LocalDateTime

/**
 * @author artur
 */
class DummyTask(val message: String?, val category: String?, val location: String?, val description: String?,
				val startTime: LocalDateTime?, val endTime: LocalDateTime?) : DummyEntry()

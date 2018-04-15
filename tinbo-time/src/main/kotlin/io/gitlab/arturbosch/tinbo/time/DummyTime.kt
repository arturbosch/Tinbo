package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.model.DummyEntry
import java.time.LocalDate

/**
 * @author artur
 */
class DummyTime(var category: String?,
                var message: String?,
                var hours: Long?,
                var minutes: Long?,
                var seconds: Long?,
                var date: LocalDate?) : DummyEntry()

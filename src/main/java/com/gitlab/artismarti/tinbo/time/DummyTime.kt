package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.common.DummyEntry
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

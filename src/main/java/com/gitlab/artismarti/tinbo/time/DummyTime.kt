package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.common.DummyEntry
import java.time.LocalDate

/**
 * @author artur
 */
class DummyTime(var category: String = "",
                var message: String = "",
                var hours: Long = -1L,
                var minutes: Long = -1L,
                var seconds: Long = -1L,
                var date: LocalDate? = null) : DummyEntry()

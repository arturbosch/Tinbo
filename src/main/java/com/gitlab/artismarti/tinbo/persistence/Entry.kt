package com.gitlab.artismarti.tinbo.persistence

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.gitlab.artismarti.tinbo.timer.TimerEntry

/**
 * @author artur
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = TimerEntry::class, name = "timerEntry")
)
abstract class Entry() {}

package com.gitlab.artismarti.tinbo.timer

import java.time.Duration
import java.time.LocalDateTime

/**
 * Domain model for timers. Specifies by a mode, start and end time.
 * Methods are provided to test if the timer is invalid or finished.
 * A convenient static calc end time method is provided.
 *
 * @author artur
 */
class Timer {

    constructor(timerMode: TimerMode = TimerMode.INVALID,
                name: String,
                startDateTime: LocalDateTime = LocalDateTime.now(),
                stopDateTime: LocalDateTime? = null) {
        this.timerMode = timerMode
        this.name = name
        this.startDateTime = startDateTime
        this.stopDateTime = stopDateTime
    }

    val name: String
    val timerMode: TimerMode
    val startDateTime: LocalDateTime
    val stopDateTime: LocalDateTime?

    override fun toString(): String {
        val (diffSecs, diffMins, diffHours) = getTimeTriple()
        return "${diffHours.toNumberString()}:${diffMins.toNumberString()}:${diffSecs.toNumberString()} ($name)"
    }

    fun getTimeTriple(): Triple<Long, Long, Long> {
        val now = LocalDateTime.now()
        val diffSecs = Duration.between(startDateTime, now).seconds.mod(60)
        val diffMins = Duration.between(startDateTime, now).toMinutes().mod(60)
        val diffHours = Duration.between(startDateTime, now).toHours().mod(60)
        return Triple(diffSecs, diffMins, diffHours)
    }

    fun isInvalid(): Boolean {
        return this.equals(INVALID)
    }

    fun isFinished(): Boolean {
        if (stopDateTime == null) {
            return false
        }
        return LocalDateTime.now().compareTo(stopDateTime) >= 0
    }

    companion object {

        val INVALID = Timer(TimerMode.INVALID, "INVALID")

        fun calcStopTime(mins: Int, seconds: Int): LocalDateTime? {
            var stop: LocalDateTime? = null
            if (mins >= 0 && seconds > 0) {
                stop = LocalDateTime.now()
                        .plusMinutes(mins.toLong())
                        .plusSeconds(seconds.toLong())
            }
            return stop
        }

    }
}

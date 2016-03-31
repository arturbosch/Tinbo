package com.gitlab.artismarti.tinbo.start

/**
 * @author artur
 */
object ModeAdvisor {

    private var mode = Mode.START

    fun setTimerMode() {
        mode = Mode.TIMER
    }

    fun isTimerMode(): Boolean {
        return mode == Mode.TIMER
    }

    fun setStartMode() {
        mode = Mode.START
    }

    fun isStartMode(): Boolean {
        return mode == Mode.START
    }
}

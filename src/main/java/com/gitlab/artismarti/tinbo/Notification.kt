package com.gitlab.artismarti.tinbo

/**
 * @author artur
 */
object Notification {

    fun finished(message: String) {
        try {
            ProcessBuilder("notify-send", "Finished", message, "--icon=dialog-information").start();
        } catch (e: Exception ) {
            println("Could not execute 'notify-send', install it to get notifications about finished timers.")
        }

    }
}

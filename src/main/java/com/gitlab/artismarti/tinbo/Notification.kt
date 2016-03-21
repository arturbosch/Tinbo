package com.gitlab.artismarti.tinbo

/**
 * @author artur
 */
object Notification {

    fun info() {
        try {
            ProcessBuilder("notify-send", "hello world", "THIS IS NOTIFICATION!", "--icon=dialog-information").start();
        } catch (e: Exception ) {
            println("Could not execute 'notify-send', install it to get notifications about running out timers.")
        }

    }
}

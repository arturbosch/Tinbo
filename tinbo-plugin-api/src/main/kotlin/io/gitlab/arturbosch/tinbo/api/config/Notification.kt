package io.gitlab.arturbosch.tinbo.api.config

import java.io.IOException
import java.io.UncheckedIOException

/**
 * @author artur
 */
object Notification {

	private const val NOTIFY_SEND_ERROR =
			"Could not execute 'notify-send', install 'libnotify' to get notifications about finished timers."
	private const val MSG_ERROR = "Could not send notification via msg.exe."

	fun notify(header: String, message: String) {
		if (System.getProperty("os.name").contains("Windows")) {
			try {
				ProcessBuilder("msg", "*", "$header - $message").start()
			} catch (e: IOException) {
				println(MSG_ERROR)
				throw UncheckedIOException(e)
			}
		} else {
			try {
				ProcessBuilder("notify-send", header, message, "--icon=dialog-information").start()
			} catch (e: IOException) {
				println(NOTIFY_SEND_ERROR)
				throw UncheckedIOException(e)
			}
		}
	}
}

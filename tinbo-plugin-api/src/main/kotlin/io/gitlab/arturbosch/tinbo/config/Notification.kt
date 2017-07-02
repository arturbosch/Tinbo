package io.gitlab.arturbosch.tinbo.config

import org.apache.log4j.LogManager
import java.io.IOException

/**
 * @author artur
 */
object Notification {

	private val logger = LogManager.getLogger(javaClass)

	private val NOTIFY_SEND_ERROR = "Could not execute 'notify-send', install it to get notifications about finished timers."

	fun notify(header: String, message: String) {
		try {
			ProcessBuilder("notify-send", header, message, "--icon=dialog-information").start()
		} catch (e: IOException) {
			println(NOTIFY_SEND_ERROR)
			logger.error(NOTIFY_SEND_ERROR, e)
		}
	}

}

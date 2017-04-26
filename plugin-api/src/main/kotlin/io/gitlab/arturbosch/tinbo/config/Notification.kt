package io.gitlab.arturbosch.tinbo.config

import org.apache.log4j.LogManager

/**
 * @author artur
 */
object Notification {

	private val logger = LogManager.getLogger(javaClass)

	private val NOTIFY_SEND_ERROR = "Could not execute 'notify-send', install it to get notifications about finished timers."

	fun notify(header: String, message: String) {
		try {
			ProcessBuilder("notify-send", header, message, "--icon=dialog-information").start()
		} catch (e: Exception) {
			println(NOTIFY_SEND_ERROR)
			logger.error(NOTIFY_SEND_ERROR, e)
		}
	}

	private val WEATHER_COMMAND_ERROR = "Could not successfully run the weather command for unknown reasons."

	fun weather(city: String): String {
		try {
			val process = ProcessBuilder("curl", "-4", "http://wttr.in/$city").start()
			return process.inputStream.buffered()
					.bufferedReader()
					.lines()
					.reduce("", { s1, s2 -> "$s1\n$s2" })
		} catch (e: Exception) {
			logger.error(WEATHER_COMMAND_ERROR, e)
			return WEATHER_COMMAND_ERROR
		}
	}
}

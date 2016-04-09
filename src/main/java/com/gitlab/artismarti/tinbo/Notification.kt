package com.gitlab.artismarti.tinbo

/**
 * @author artur
 */
object Notification {

	fun finished(message: String) {
		try {
			ProcessBuilder("notify-send", "Finished", message, "--icon=dialog-information").start();
		} catch (e: Exception) {
			println("Could not execute 'notify-send', install it to get notifications about finished timers.")
		}

	}

	fun weather(city: String): String {
		if (city.matches(Regex("[a-zA-Z]+"))) {
			try {
				val process = ProcessBuilder("curl", "-4", "http://wttr.in/$city").start()
				return process.inputStream.buffered()
						.bufferedReader()
						.lines()
						.reduce("", { s1, s2 -> "$s1\n$s2" })
			} catch(e: Exception) {
				return "Could not successfully run the weather command for unknown reasons."
			}
		} else {
			return "The given city name must consist of only letters."
		}
	}
}

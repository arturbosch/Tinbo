package io.gitlab.arturbosch.tinbo.weather

import org.apache.log4j.LogManager
import java.io.IOException
import java.io.InputStream

/**
 * @author Artur Bosch
 */
object Weather {

	private val logger = LogManager.getLogger(javaClass)
	private const val WEATHER_COMMAND_ERROR = "Could not successfully run the weather command for unknown reasons."

	fun curl(city: String): String {
		return try {
			val process = ProcessBuilder("curl", "-4", "http://wttr.in/$city").start()
			val error = process.errorStream.readToString()
			val output = process.inputStream.readToString()
			if (output.isEmpty()) error else output
		} catch (e: IOException) {
			logger.error(WEATHER_COMMAND_ERROR, e)
			WEATHER_COMMAND_ERROR
		}
	}

	private fun InputStream.readToString() = bufferedReader().lines()
			.reduce("", { s1, s2 -> "$s1\n$s2" })
}

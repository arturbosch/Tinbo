package com.gitlab.artismarti.tinbo.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * @author artur
 */
object DateTimeFormatters {

	fun parseDateTimeOrDefault(endTime: String, startTime: String): Pair<LocalDateTime?, LocalDateTime?> {
		var formattedStartTime: LocalDateTime? = null
		var formattedEndTime = formattedStartTime
		try {
			formattedStartTime = LocalDateTime.parse(startTime, dateTimeFormatter)
		} catch(ignored: DateTimeParseException) {
		}
		if (endTime.isNotEmpty()) {
			try {
				formattedEndTime = LocalDateTime.parse(endTime, dateTimeFormatter)
			} catch(ignored: DateTimeParseException) {
			}
		}
		return Pair(formattedStartTime, formattedEndTime)
	}

	fun parseDateTime(endTime: String, startTime: String): Pair<LocalDateTime, LocalDateTime> {
		val formattedStartTime = LocalDateTime.parse(startTime, dateTimeFormatter)
		var formattedEndTime = formattedStartTime
		if (endTime.isNotEmpty()) formattedEndTime = LocalDateTime.parse(endTime, dateTimeFormatter)
		return Pair(formattedStartTime, formattedEndTime)
	}

	fun parseDateOrNull(startTime: String): LocalDate? {
		return try {
			LocalDate.parse(startTime, dateFormatter)
		} catch(e: DateTimeParseException) {
			null
		}
	}

}

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


package io.gitlab.arturbosch.tinbo.csv

import io.gitlab.arturbosch.tinbo.model.Entry
import io.gitlab.arturbosch.tinbo.replaceSeparator
import org.joda.money.Money
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.util.ArrayList

/**
 * @author artur
 */
class CSVDataExchange {

	private val separator = ";"

	fun <T : Entry> toCSV(entries: List<T>): List<String> {
		return entries.map { entry ->
			entry.javaClass.declaredFields.asList()
					.map {
						it.isAccessible = true
						it.get(entry)
					}
					.joinToString(separator) { it.toString().replaceSeparator() }
		}

	}

	fun toCSV2(entries: List<Entry>): List<String> {
		return entries.map { it.toString() }
	}

	fun <T : Entry> fromCSV(clazz: Class<T>, entriesAsString: List<String>): List<T> {
		val result = ArrayList<T>()
		val fields = getAccessibleFields(clazz)
		for (entryString in entriesAsString) {
			val instance = clazz.newInstance()
			val fieldData = entryString.split(Regex.fromLiteral(separator))
			if (fieldData.size == fields.size) {
				for ((index, data) in fieldData.withIndex()) {
					val transformedData = transformData(data, fields, index)
					fields[index].set(instance, transformedData)
				}
				result.add(instance)
			}
		}
		return result
	}

	private fun <T : Entry> getAccessibleFields(clazz: Class<T>) =
			clazz.declaredFields.asList().apply { map { it.isAccessible = true } }

	@Suppress("IMPLICIT_CAST_TO_ANY")
	private fun transformData(data: String, fields: List<Field>, index: Int) =
			when (fields[index].type) {
				Int::class.java -> data.toInt()
				Long::class.java -> data.toLong()
				Month::class.java -> Month.valueOf(data)
				Money::class.java -> Money.parse(data)
				Boolean::class.java -> data.toBoolean()
				Double::class.java -> data.toDouble()
				Float::class.java -> data.toFloat()
				LocalDate::class.java -> LocalDate.parse(data)
				LocalDateTime::class.java -> LocalDateTime.parse(data)
				else -> data
			}
}

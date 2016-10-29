package io.gitlab.arturbosch.tinbo

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @author Artur Bosch
 */

data class Project(var name: String = "",
				   var description: String = "",
				   var start: LocalDate = LocalDate.now(),
				   var end: LocalDate? = null,
				   var tasks: List<Task> = listOf()) {
	fun sumPlannedTime(): Int = tasks.sumBy { it.plannedTime.minutes }
	fun sumPlannedUnits(): Int = tasks.sumBy(Task::plannedUnits)
	fun sumActualTime(): Int = tasks.sumBy { it.actualTime?.minutes ?: 0 }
	fun sumActualUnits(): Int = tasks.sumBy { it.actualUnits ?: 0 }

	fun csvHeader(): String = "Name;pTime;aTime;pUnits;aUnits;Start;End;Description"
	fun asCSV(): String = "$name;${sumPlannedTime().asHourString()};${sumActualTime().asHourString()};" +
			"${sumPlannedUnits()};${sumActualUnits()};${dateFormatter.format(start)};" +
			"${formatEndDate()};${description.orSpace()}"

	private fun formatEndDate() = if (end == null) "undef" else dateFormatter.format(end)

	fun add(task: Task) {
		tasks = tasks.plus(task)
	}
}

fun String.orSpace(): String = if (this.isNullOrEmpty()) " " else this
val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
fun Int.asHourString(): String {
	return "${this / 60}h${this % 60}m"
}


data class Task(var name: String = "",
				var plannedUnits: Int = 0,
				var actualUnits: Int? = null,
				var plannedTime: Time = Time(),
				var actualTime: Time? = null,
				var end: LocalDate = LocalDate.now(),
				var time: List<Time> = listOf()) {
	fun csvHeader(): String = "Name;pTime;aTime;pUnits;aUnits;End;Done?"
	fun asCSV(): String = "$name;${plannedTime.minutes.asHourString()};" +
			"${actualTime?.minutes?.asHourString() ?: "undef"};" +
			"$plannedUnits;${safeActual()};" +
			"${dateFormatter.format(end)};${actualTime != null && actualUnits != null}"

	private fun safeActual() = if (actualUnits == null || actualUnits == 0)
		"undef" else actualUnits.toString()

	fun complete(minutes: Int, units: Int) {
		actualTime = Time(minutes)
		actualUnits = units
	}
}

data class Time(var minutes: Int = 0,
				var breaks: Int = 0,
				var comment: String = "")
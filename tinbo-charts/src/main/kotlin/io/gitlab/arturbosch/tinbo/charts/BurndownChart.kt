package io.gitlab.arturbosch.tinbo.charts

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.model.Project
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import org.jfree.chart.title.TextTitle
import org.jfree.data.category.DefaultCategoryDataset
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.time.LocalDate
import java.time.Period

/**
 * @author Artur Bosch
 */
@Component
class BurndownChart(private val tinbo: TinboContext) : Command {

	override val id: String = "plugins"

	@CliCommand("charts burndown", help = "Choose a project to generate a burndown chart based on psp units.")
	fun burndown(@CliOption(key = [""]) name: String?) {
		loadSummary("projects")?.find { project ->
			name?.let { project.name.startsWith(name) } ?: false
		}?.let { project ->

			val start = project.start
			val end = project.end ?: throw IllegalArgumentException("Project must have an end date!")
			val period = Period.between(start, end)
			val multiplier = calculateMultiplier(period)
			val data = DefaultCategoryDataset()
			addPlannedValuesToDataset(data, end, project, multiplier, start)
			addActualValuesToDataset(data, end, project, multiplier, start)
			addProjectedValuesToDataset(data, end, project, multiplier)
			val chart = customizeChart(data, project)
			showChart(chart, "Tinbo Chart - Burndown")

		} ?: println("No project with given name found!")
	}

	private fun calculateMultiplier(period: Period): Int {
		return when (period.days + period.months * 30 + period.years * 365) {
			in 1..10 -> 1
			in 11..30 -> 2
			in 31..60 -> 3
			in 61..90 -> 4
			in 91..120 -> 5
			else -> 7
		}
	}

	private fun customizeChart(data: DefaultCategoryDataset, project: Project): JFreeChart {
		val chart = ChartFactory.createLineChart("Burndown Chart for project: ${project.name}", "Time", "Units", data)
		chart.addSubtitle(TextTitle("${project.start} - ${project.end}"))
		val plot = chart.categoryPlot
		plot.domainAxis.tickLabelFont = Font("Dialog", Font.PLAIN, 8)
		val renderer = plot.renderer as LineAndShapeRenderer
		renderer.setSeriesStroke(2, BasicStroke(
				1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
				1.0f, floatArrayOf(6.0f, 6.0f), 0.0f))
		renderer.setSeriesPaint(1, Color.BLUE)
		renderer.setSeriesPaint(2, Color.BLUE)
		return chart
	}

	private fun projectedUnitsPerDay(project: Project, now: LocalDate): Int {
		val completedTasks = project.tasks.filter { it.completeDate != null }
		val summedUnits = completedTasks.sumBy { it.actualUnits ?: 0 }
		val minDate = completedTasks.fold(now) { minDate, task -> if (task.end < minDate) task.end else minDate }
		val maxDate = completedTasks.fold(now) { maxDate, task -> if (task.end > maxDate) task.end else maxDate }
		val days = Period.between(minDate, maxDate).days
		val unitsPerDay = summedUnits / days
		return if (unitsPerDay < 1) 1 else unitsPerDay
	}

	private fun addProjectedValuesToDataset(data: DefaultCategoryDataset, end: LocalDate, project: Project, multiplier: Int) {
		val today = LocalDate.now()
		val unitsPerDay = projectedUnitsPerDay(project, today)
		val projected = "projected"
		var timeAxis = today
		var units = project.sumActualUnitsAfter(today).toLong()
		do {
			data.addValue(if (units < 0) 0 else units, projected, timeAxis.toDateFormat())
			units -= unitsPerDay * multiplier
			timeAxis = timeAxis.plusDays(multiplier.toLong())
		} while (timeAxis <= end)
	}

	private fun addActualValuesToDataset(data: DefaultCategoryDataset, end: LocalDate, project: Project, multiplier: Int, start: LocalDate) {
		val today = LocalDate.now()
		val actual = "actual"
		var timeAxis = start
		do {
			data.addValue(project.sumActualUnitsAfter(timeAxis), actual, timeAxis.toDateFormat())
			timeAxis = timeAxis.plusDays(multiplier.toLong())
		} while (timeAxis < end && timeAxis < today)
		data.addValue(project.sumActualUnitsAfter(end), actual, timeAxis.toDateFormat())
	}

	private fun addPlannedValuesToDataset(data: DefaultCategoryDataset, end: LocalDate, project: Project, multiplier: Int, start: LocalDate) {
		val planned = "planned"
		var timeAxis = start
		do {
			data.addValue(project.sumPlannedUnitsAfter(timeAxis), planned, timeAxis.toDateFormat())
			timeAxis = timeAxis.plusDays(multiplier.toLong())
		} while (timeAxis < end)
		data.addValue(0, planned, end.toDateFormat())
	}

	@Suppress("UNCHECKED_CAST")
	private fun loadSummary(method: String): List<Project>? {
		tinbo.also {
			it.helpers.find { it.javaClass.simpleName == "ProjectsPluginSupport" }?.let {
				val value = it.javaClass.getMethod(method).invoke(it)
				return value as List<Project>
			}
		}
		return null
	}

}

package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.title.TextTitle
import org.jfree.data.category.DefaultCategoryDataset
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import java.awt.Dimension
import java.awt.Font
import java.time.LocalDate
import java.time.Period
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

/**
 * @author Artur Bosch
 */
class BurndownChart : TiNBoPlugin {

	@CliCommand("charts burndown")
	fun burndown(@CliOption(key = arrayOf("")) name: String?) {
		loadSummary("projects")?.find { project ->
			name?.let { project.name.startsWith(name) } ?: false
		}?.let {
			val start = it.start
			val end = it.end ?: throw IllegalArgumentException("Project must have an end date!")
			val period = Period.between(start, end)
			val multiplier = calculateMultiplier(period)
			val data = DefaultCategoryDataset()
			addPlannedValuesToDataset(data, end, it, multiplier, start)
			addActualValuesToDataset(data, end, it, multiplier, start)
			val chart = chartWithCustomFonts(data, it)
			showImage(chart, "Tinbo Chart - Burndown")

		} ?: println("No project with given name found!")
	}

	private fun calculateMultiplier(period: Period): Long {
		return when (period.days + period.months * 30 + period.years * 365) {
			in 1..10 -> 1L
			in 11..30 -> 2L
			in 31..60 -> 3L
			in 61..90 -> 4L
			in 91..120 -> 5L
			else -> 7L
		}
	}

	private fun chartWithCustomFonts(data: DefaultCategoryDataset, it: Project): JFreeChart {
		val chart = ChartFactory.createLineChart("Burndown Chart for project", "Time", "Units", data)
		chart.addSubtitle(TextTitle("${it.name}: ${it.start} - ${it.end}"))
		chart.categoryPlot.rangeAxis.labelFont = Font("Dialog", Font.PLAIN, 8)
		chart.categoryPlot.domainAxis.labelFont = Font("Dialog", Font.PLAIN, 8)
		chart.categoryPlot.domainAxis.tickLabelFont = Font("Dialog", Font.PLAIN, 8)
		return chart
	}

	private fun addActualValuesToDataset(data: DefaultCategoryDataset, end: LocalDate, it: Project, multiplier: Long, start: LocalDate) {
		val today = LocalDate.now()
		val actual = "actual"
		var timeAxis = start
		do {
			data.addValue(it.sumActualUnitsAfter(timeAxis), actual, timeAxis.toDateFormat())
			timeAxis = timeAxis.plusDays(multiplier)
		} while (timeAxis < end && timeAxis < today)
		data.addValue(it.sumActualUnitsAfter(end), actual, timeAxis.toDateFormat())
	}

	private fun addPlannedValuesToDataset(data: DefaultCategoryDataset, end: LocalDate, it: Project, multiplier: Long, start: LocalDate) {
		val planned = "planned"
		var timeAxis = start
		do {
			data.addValue(it.sumPlannedUnitsAfter(timeAxis), planned, timeAxis.toDateFormat())
			timeAxis = timeAxis.plusDays(multiplier)
		} while (timeAxis < end)
		data.addValue(0, planned, end.toDateFormat())
	}

	@Suppress("UNCHECKED_CAST")
	private fun loadSummary(method: String): List<Project>? {
		context()?.let {
			it.helpers.find { it.javaClass.simpleName == "ProjectsPluginSupport" }?.let {
				val value = it.javaClass.getMethod(method).invoke(it)
				return value as List<Project>
			}
		}
		return null
	}

}

fun showImage(chart: JFreeChart, title: String) {
	SwingUtilities.invokeLater {
		JFrame(title).apply {
			defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
			add(ChartPanel(chart).apply {
				preferredSize = Dimension(1280, 760)
			})
			pack()
		}.isVisible = true
	}
}

private fun LocalDate.toDateFormat(): Comparable<DateChartFormat> = DateChartFormat(this)

class DateChartFormat(val date: LocalDate) : Comparable<DateChartFormat> {
	override fun compareTo(other: DateChartFormat): Int {
		return date.compareTo(other.date)
	}

	override fun toString(): String {
		return "${date.dayOfMonth}.${date.monthValue}"
	}

	override fun equals(other: Any?): Boolean {
		return if (other is DateChartFormat) date == other.date else false
	}

	override fun hashCode(): Int {
		return date.hashCode()
	}
}

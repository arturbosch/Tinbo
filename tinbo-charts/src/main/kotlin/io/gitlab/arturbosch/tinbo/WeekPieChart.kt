package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.title.TextTitle
import org.jfree.data.general.DefaultPieDataset
import org.springframework.shell.core.annotation.CliCommand
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

/**
 * @author Artur Bosch
 */
class WeekPieChart : TiNBoPlugin {

	@CliCommand("charts week", help = "Generates a chart illustrates time spent in current week.")
	fun week() {
		val method = "Week"
		loadSummary("week")?.let { week ->
			createChartForSummary(method, week)
		}
	}

	@CliCommand("charts month", help = "Generates a chart illustrates time spent in current month.")
	fun month() {
		val method = "Month"
		loadSummary("month")?.let { month ->
			createChartForSummary(method, month)
		}
	}

	private fun createChartForSummary(method: String, week: WeekSummary) {
		val data = DefaultPieDataset().apply {
			week.entries.forEach {
				val (h, m) = it.asHourMinutes()
				this.setValue(it.category + " - ${h}h${m}m", it.minutes)
			}
		}
		val chart = ChartFactory.createPieChart("$method: ${week.asDateRangeString()}",
				data, false, true, false)
		val (hour, mins) = week.totalHourMinutes()
		chart.addSubtitle(TextTitle("Total ${week.totalMinutes()}m / ${hour}h${mins}m"))
		SwingUtilities.invokeLater {
			JFrame("Tinbo Chart - $method").apply {
				defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
				add(ChartPanel(chart).apply {
					preferredSize = Dimension(640, 480)
				})
				pack()
			}.isVisible = true
		}
	}

	private fun loadSummary(method: String): WeekSummary? {
		context()?.let {
			it.helpers.find { it.javaClass.simpleName == "TimeSummaryPluginHelper" }?.let {
				return it.javaClass.getMethod(method).invoke(it) as WeekSummary
			}
		}
		return null
	}

}
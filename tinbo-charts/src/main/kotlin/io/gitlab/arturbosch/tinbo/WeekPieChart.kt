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
	fun run() {
		loadSummary()?.let { week ->
			val data = DefaultPieDataset().apply {
				week.entries.forEach {
					val (h, m) = it.asHourMinutes()
					this.setValue(it.category + " - ${h}h${m}m", it.minutes)
				}
			}
			val chart = ChartFactory.createPieChart("Week: ${week.asDateRangeString()}",
					data, false, true, false)
			val (hour, mins) = week.totalHourMinutes()
			chart.addSubtitle(TextTitle("Total ${week.totalMinutes()}m / ${hour}h${mins}m"))
			SwingUtilities.invokeLater {
				JFrame("Tinbo Chart - Week").apply {
					defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
					add(ChartPanel(chart).apply {
						preferredSize = Dimension(320, 240)
					})
					pack()
				}.isVisible = true
			}
		}
	}

	private fun loadSummary(): WeekSummary? {
		context()?.let {
			it.commands.find { it.javaClass.simpleName == "TimeSummaryCommands" }?.let {
				return it.javaClass.getMethod("forChartTest").invoke(it) as WeekSummary
			}
		}
		return null
	}

}
package io.gitlab.arturbosch.tinbo.charts

import io.gitlab.arturbosch.tinbo.WeekSummary
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.ContextAware.context
import org.jfree.chart.ChartFactory
import org.jfree.chart.title.TextTitle
import org.jfree.data.general.DefaultPieDataset
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class WeekMonthChart : Command {

	override val id: String = "plugins"

	@CliCommand("charts day", help = "Generates a chart illustrates time spent in current day.")
	fun day() {
		val method = "Day"
		loadSummary("day")?.let { week ->
			createChartForSummary(method, week)
		}
	}

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
		showChart(chart, "Tinbo Chart - $method")
	}

	private fun loadSummary(method: String): WeekSummary? {
		context.let {
			it.pluginHelpers().find { it.javaClass.simpleName == "TimeSummaryPluginSupport" }?.let {
				return it.javaClass.getMethod(method).invoke(it) as WeekSummary
			}
		} ?: return null
	}

}

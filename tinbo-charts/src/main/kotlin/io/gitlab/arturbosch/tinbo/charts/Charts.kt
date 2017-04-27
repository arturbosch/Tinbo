package io.gitlab.arturbosch.tinbo.charts

import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

/**
 * Displays given chart inside a swing JFrame.
 *
 * @author Artur Bosch
 */
fun showChart(chart: JFreeChart, title: String) {
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

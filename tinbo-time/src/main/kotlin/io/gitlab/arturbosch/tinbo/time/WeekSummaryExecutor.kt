package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.model.CSVAwareExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
open class WeekSummaryExecutor @Autowired constructor(private val timeDataHolder: TimeDataHolder) :
		CSVAwareExecutor() {

	override val tableHeader: String
		get() = "No.;Category;Time"

	fun asTable(summaries: List<String>): String = tableAsString(summaries, tableHeader)

	fun sumAllCategories(): String {
		val summaries = timeDataHolder.createSummaries()
		return asTable(summaries)
	}

	fun sumForCategories(filters: List<String>): String {
		val summaries = timeDataHolder.createFilteredSummaries(filters)
		return asTable(summaries)
	}

}

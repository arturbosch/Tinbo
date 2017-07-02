package io.gitlab.arturbosch.tinbo.model

import io.gitlab.arturbosch.tinbo.model.util.CSVTablePrinter
import io.gitlab.arturbosch.tinbo.plusElementAtBeginning
import io.gitlab.arturbosch.tinbo.withIndexedColumn

/**
 * @author Artur Bosch
 */
abstract class CSVAwareExecutor {

	protected abstract val TABLE_HEADER: String
	protected val csv = CSVTablePrinter()

	fun tableAsString(summaries: List<String>, header: String = TABLE_HEADER): String {
		return csv.asTable(
				summaries.withIndexedColumn()
						.plusElementAtBeginning(header)
		).joinToString(separator = "\n")
	}
}
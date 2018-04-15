package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.model.Project
import io.gitlab.arturbosch.tinbo.api.model.CSVAwareExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class CSVProjects @Autowired constructor(private val fileProjects: FileProjects) : CSVAwareExecutor() {

	private val dummy = Project()
	override val TABLE_HEADER: String = "No.;" + dummy.csvHeader()

	fun convert(): String {
		val sums = fileProjects.projects.map { it.asCSV() }
		return tableAsString(sums)
	}

}

package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.model.Project
import io.gitlab.arturbosch.tinbo.api.model.Task
import org.junit.Test
import java.time.LocalDate

/**
 * @author Artur Bosch
 */
class ProjectPersistenceTest {

	@Test
	fun save() {
		val before = Project("PSP", "Commands for Tinbo",
				LocalDate.now(), LocalDate.now().plusDays(1),
				listOf(Task("1"), Task("2"), Task("3")))
		val saved = before.toYaml()
		val after = saved.fromYaml()
		assert(before == after)
	}

}

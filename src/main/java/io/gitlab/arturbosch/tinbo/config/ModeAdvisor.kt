package io.gitlab.arturbosch.tinbo.config

import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
object ModeAdvisor {

	private var mode = Mode.START

	fun getMode() = mode

}

package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.providers.BannerProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class VersionCommand @Autowired constructor(val provider: BannerProvider) : Command {

	override val id: String = "share"

	@CliCommand(value = *arrayOf("version"), help = "Shows current Tinbo version.")
	fun version(): String {
		return provider.version
	}

}

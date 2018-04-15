package io.gitlab.arturbosch.tinbo.providers

import com.google.common.io.Resources
import io.gitlab.arturbosch.tinbo.TinboTerminal
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.shell.plugin.support.DefaultBannerProvider
import org.springframework.shell.support.util.OsUtils
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class BannerProvider(private val terminal: TinboTerminal,
					 private val env: Environment) : DefaultBannerProvider() {

	@EventListener(ContextRefreshedEvent::class)
	private fun onStartup() {
		terminal.write(banner + "\n" + welcomeMessage)
	}

	override fun getBanner(): String {
		return Resources.readLines(Resources.getResource("banner.txt"),
				Charsets.UTF_8).joinToString(separator = OsUtils.LINE_SEPARATOR)
	}

	override fun getVersion(): String {
		return "Tinbo v${env.getProperty("tinbo.version")}"
	}

	override fun getWelcomeMessage(): String {
		return "Welcome to $version. Use 'help' to view all commands!"
	}

	override fun getProviderName(): String {
		return "Tinbo"
	}
}

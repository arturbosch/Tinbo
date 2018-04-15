package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.ConfigurableCommandRegistry
import org.springframework.shell.MethodTargetRegistrar
import org.springframework.stereotype.Component
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
@Component
class PluginRegistry @Autowired constructor(
		private val context: TinboContext,
		private val console: TinboTerminal) : MethodTargetRegistrar {

	val shellCommands = ArrayList<Command>()
	private val registeredPlugins = ArrayList<TinboPlugin>()
	private val log = LogManager.getLogger(javaClass)

	override fun register(registry: ConfigurableCommandRegistry) {
		val plugins = loadPlugins()
		for (plugin in plugins) {
			shellCommands.addAll(plugin.registerCommands(context))
		}
		if (plugins.isNotEmpty()) {
			console.write("Successfully loaded plugins: ${plugins.joinToString(", ")}")
		}
	}

	private fun loadPlugins(): List<TinboPlugin> {
		ContextAware.context = context
		try {
			val pluginsPath = HomeFolder.getDirectory(HomeFolder.PLUGINS)
			val pluginFiles = pluginsPath.toFile().listFiles()
			if (pluginFiles.isNotEmpty()) {
				val jarUrls = pluginFiles
						.filter { it.name.endsWith(".jar") }
						.map { it.toURI().toURL() }
				val loader = URLClassLoader(jarUrls.toTypedArray(), TinboPlugin::class.java.classLoader)
				return ServiceLoader.load(TinboPlugin::class.java, loader).toList().apply {
					registeredPlugins.addAll(this)
				}
			}
		} catch (e: RuntimeException) {
			console.write("Could not load plugins: ${e.message}")
			log.error("Could not load plugins: ${e.message}", e)
		}
		return emptyList()
	}
}

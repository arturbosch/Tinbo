package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.api.TinboBus
import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.config.HomeFolder
import io.gitlab.arturbosch.tinbo.api.config.ModeListener
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.marker.EventBusSubscriber
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
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

	val plugins: List<TinboPlugin> get() = registeredPlugins
	val commands: List<Command> get() = shellCommands

	private val shellCommands = ArrayList<Command>()
	private val registeredPlugins = ArrayList<TinboPlugin>()
	private val log = LogManager.getLogger(javaClass)

	override fun register(registry: ConfigurableCommandRegistry) {
		val plugins = loadPlugins()
		for (plugin in plugins) {
			registerPlugin(plugin)
		}
		notifyPluginsLoaded(plugins)
		registerEventBusSubscriber()
	}

	private fun registerPlugin(plugin: TinboPlugin) {
		for (command in plugin.registerCommands(context)) {
			if (!context.hasBean(command)) {
				context.registerSingleton(command)
			}
			shellCommands.add(command)
		}
	}

	private fun notifyPluginsLoaded(plugins: List<TinboPlugin>) {
		if (plugins.isNotEmpty()) {
			console.write("Successfully loaded plugins: ${plugins.joinToString(", ")}")
		}
	}

	private fun registerEventBusSubscriber() {
		for (subscriber in context.beansOf<EventBusSubscriber>().values) {
			TinboBus.register(subscriber)
		}
	}

	private fun loadPlugins(): List<TinboPlugin> {
		try {
			val pluginsPath = HomeFolder.getDirectory(HomeFolder.PLUGINS)
			val pluginFiles = pluginsPath.toFile().listFiles()
			if (pluginFiles.isNotEmpty()) {
				val jarUrls = pluginFiles
						.filter { it.name.endsWith(".jar") }
						.map { it.toURI().toURL() }
				val loader = URLClassLoader(jarUrls.toTypedArray(), TinboPlugin::class.java.classLoader)
				ServiceLoader.load(ModeListener::class.java, loader).forEach {
					ModeManager.register(it)
				}
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

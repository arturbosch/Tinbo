package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.MethodTarget
import org.springframework.stereotype.Component
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.ServiceLoader
import java.util.TreeMap
import javax.annotation.PostConstruct

/**
 * @author Artur Bosch
 */
@Component
class PluginRegistry @Autowired constructor(
		private val context: TinboContext) {

	val shellCommands = ArrayList<Command>()
	private val successfulPlugins = ArrayList<TinboPlugin>()
	private val log = LogManager.getLogger(javaClass)
	private val commands = TreeMap<String, MethodTarget>()

	@PostConstruct
	private fun loadPlugins() {
		val plugins = loadPluginCommands()
		for (plugin in plugins) {
			context.registerSingleton(plugin.javaClass.name, plugin)
		}
		notifyLoaded()
	}

	private fun notifyLoaded() {
		if (successfulPlugins.isNotEmpty()) {
			printlnInfo("Successfully loaded plugins: ${successfulPlugins.joinToString(", ")}")
		}
	}

	private fun loadPluginCommands(): List<TinboPlugin> {
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
					successfulPlugins.addAll(this)
				}
			}
		} catch (e: RuntimeException) {
			printlnInfo("Could not load plugins: ${e.message}")
			log.error("Could not load plugins: ${e.message}", e)
		}
		return emptyList()
	}
}

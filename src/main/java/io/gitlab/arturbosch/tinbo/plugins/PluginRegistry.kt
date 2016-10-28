package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.JLineShellComponent
import org.springframework.stereotype.Component
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.ServiceLoader
import javax.annotation.PostConstruct

/**
 * @author artur
 */
@Component
class PluginRegistry @Autowired constructor(val shell: JLineShellComponent,
											val context: SpringContext) :
		ClassLoader(TiNBoPlugin::class.java.classLoader) {

	private val LOGGER = LogManager.getLogger(javaClass)

	@PostConstruct
	fun postConstruct() {
		try {
			val pluginsPath = HomeFolder.getDirectory(HomeFolder.PLUGINS)
			val pluginFiles = pluginsPath.toFile().listFiles()

			if (pluginFiles.isEmpty()) return
			val jarUrls = pluginFiles.filter { it.name.endsWith(".jar") }.map { it.toURI().toURL() }

			loadJarPlugins(jarUrls)
		} catch (e: RuntimeException) {
			val message = "Could not load plugins: ${e.message}"
			logError(e, message)
		}
	}

	private fun loadJarPlugins(jarUrls: List<URL>) {
		val successfulPlugins = ArrayList<String>()
		ServiceLoader.load(TiNBoPlugin::class.java, URLClassLoader(jarUrls.toTypedArray(), this))
				.forEach {
					registerPlugin(it)
					successfulPlugins.add(it.javaClass.simpleName)
				}

		if (successfulPlugins.isNotEmpty()) {
			TiNBoPlugin.ContextAware.context = context
			printlnInfo("Successfully loaded plugins: ${successfulPlugins.joinToString(", ")}")
		}
	}

	fun registerPlugin(command: CommandMarker) {
		shell.simpleParser.add(command)
	}

	private fun logError(e: Throwable, message: String) {
		printlnInfo(message)
		LOGGER.error(message, e)
	}

}
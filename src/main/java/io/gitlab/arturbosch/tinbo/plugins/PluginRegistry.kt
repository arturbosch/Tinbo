package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.JLineShellComponent
import org.springframework.stereotype.Component
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.ServiceLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import javax.annotation.PostConstruct

/**
 * @author artur
 */
@Component
class PluginRegistry @Autowired constructor(val shell: JLineShellComponent,
											val context: SpringContext) {

	private val LOGGER = LogManager.getLogger(javaClass)

	@PostConstruct
	fun postConstruct() {
		val executor = Executors.newSingleThreadExecutor()
		try {
			CompletableFuture.runAsync(Runnable {
				val pluginsPath = HomeFolder.getDirectory(HomeFolder.PLUGINS)
				val pluginFiles = pluginsPath.toFile().listFiles()

				if (pluginFiles.isNotEmpty()) {
					val jarUrls = pluginFiles
							.filter { it.name.endsWith(".jar") }
							.map { it.toURI().toURL() }
					loadJarPlugins(jarUrls)
				}
			}, executor).exceptionally {
				logError(it, it.message ?: ""); null
			}

		} catch (e: RuntimeException) {
			val message = "Could not load plugins: ${e.message}"
			logError(e, message)
		}
		executor.shutdown()
	}

	private fun loadJarPlugins(jarUrls: List<URL>) {
		val successfulPlugins = ArrayList<String>()
		val loader = URLClassLoader(jarUrls.toTypedArray(), TiNBoPlugin::class.java.classLoader)
		ServiceLoader.load(TiNBoPlugin::class.java, loader)
				.forEach {
					registerPlugin(it)
					successfulPlugins.add(it.javaClass.simpleName)
				}

		if (successfulPlugins.isNotEmpty()) {
			TiNBoPlugin.ContextAware.context = context
			printlnInfo("Successfully loaded plugins: ${successfulPlugins.joinToString(", ")}")
		}
	}

	fun registerPlugin(plugin: TiNBoPlugin) {
		plugin.registerCommands(context).forEach { shell.simpleParser.add(it) }
	}

	private fun logError(e: Throwable, message: String) {
		printlnInfo(message)
		LOGGER.error(message, e)
	}

}
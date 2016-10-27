package io.gitlab.arturbosch.tinbo.plugins

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
class PluginRegistry @Autowired constructor(val shell: JLineShellComponent) :
		ClassLoader(TiNBoPlugin::class.java.classLoader) {

	private val LOGGER = LogManager.getLogger(javaClass)

	@PostConstruct
	fun postConstruct() {
		try {
			val pluginsPath = HomeFolder.getDirectory(HomeFolder.PLUGINS)
			val pluginRegistryPath = HomeFolder.getFile(pluginsPath.resolve(HomeFolder.PLUGIN_REGISTRY))
			val pluginFiles = pluginsPath.toFile().listFiles()

			if (pluginFiles.isEmpty()) return

			val classUrls = pluginFiles.filter { it.name.endsWith(".class") }.map { it.toURI().toURL() }
			val jarUrls = pluginFiles.filter { it.name.endsWith(".jar") }.map { it.toURI().toURL() }
			val classNames = pluginRegistryPath.toFile().readLines()
					.filter { !it.toString().startsWith("#") }
					.filter { !it.toString().isBlank() }
					.toList()

			loadJarPlugins(jarUrls)
			loadPlugins(classNames, classUrls)
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
			printlnInfo("Successfully loaded plugins: ${successfulPlugins.joinToString(", ")}")
		}
	}

	private fun loadPlugins(classNames: List<String>, classUrls: List<URL>) {
		val successfulPlugins = ArrayList<String>()
		classUrls.forEach {
			val className = it.file.substringAfterLast('/').substringBeforeLast('.')
			val stream = it.openStream()
			stream.use {
				val bytes = stream.readBytes()
				if (registerClass(bytes, className, classNames)) successfulPlugins.add(className)
			}
		}
		if (successfulPlugins.isNotEmpty()) {
			printlnInfo("Successfully loaded plugins: ${successfulPlugins.joinToString(", ")}")
		}
	}

	private fun registerClass(bytes: ByteArray, className: String, classNames: List<String>): Boolean {
		try {
			val clazz = defineClass(classNames.first { it.toString().contains(className) }, bytes, 0, bytes.size)
			val newInstance = clazz.newInstance() as CommandMarker
			registerPlugin(newInstance)
			return true
		} catch (e: Throwable) {
			val message = "Fatal exception was thrown during instantiation of plugin $className: ${e.cause}"
			logError(e, message)
			return false
		}
	}

	private fun logError(e: Throwable, message: String) {
		printlnInfo(message)
		LOGGER.error(message, e)
	}

	fun registerPlugin(command: CommandMarker) {
		shell.simpleParser.add(command)
	}

}
package com.gitlab.artismarti.tinbo.plugins

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.ApplicationContext
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.JLineShellComponent
import org.springframework.stereotype.Component
import java.net.URL
import java.util.ArrayList
import javax.annotation.PostConstruct

/**
 * @author artur
 */
@Component
class PluginRegistry @Autowired constructor(val context: ApplicationContext,
											val shell: JLineShellComponent) : ClassLoader() {

	private val LOGGER = LogManager.getLogger(javaClass)

	@PostConstruct
	fun postConstruct() {
		try {
			val pluginPath = HomeFolder.getDirectory("plugins")
			val pluginRegistryFile = HomeFolder.getFile(pluginPath.resolve("PluginRegistry"))
			val plugins = pluginPath.toFile().listFiles()

			if (plugins.isEmpty()) return

			val classUrls = plugins.filter { it.name.endsWith(".class") }.map { it.toURI().toURL() }
			val classNames = pluginRegistryFile.toFile().readLines()
					.filter { !it.toString().startsWith("#") }
					.filter { !it.toString().isBlank() }
					.toList()

			loadPlugins(classNames, classUrls)
		} catch (e: RuntimeException) {
			val message = "Could not load plugins: ${e.message}"
			logError(e, message)
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
		printlnInfo("Successfully loaded plugins: ${successfulPlugins.joinToString(", ")}")
	}

	private fun registerClass(bytes: ByteArray, className: String, classNames: List<String>): Boolean {
		return try {
			val clazz = defineClass(classNames.first { it.toString().contains(className) }, bytes, 0, bytes.size)
			val newInstance = clazz.newInstance() as CommandMarker
			registerPlugin(newInstance)
			true
		} catch (e: RuntimeException) {
			val message = "Fatal exception was thrown during instantiation of plugin $className: ${e.message}"
			logError(e, message)
			false
		}
	}

	private fun logError(e: RuntimeException, message: String) {
		printlnInfo(message)
		LOGGER.error(message, e)
	}

	fun registerPlugin(command: CommandMarker) {
		shell.simpleParser.add(command)
		shell.simpleParser.commandMarkers.forEach { println(it) }
	}

}
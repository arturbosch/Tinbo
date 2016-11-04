package io.gitlab.arturbosch.tinbo.config

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
object HomeFolder {

	const val CONFIG_NAME = "config.yaml"
	const val PLUGINS: String = "plugins"
	const val BACKUP: String = "backup"

	private val mainDir = "TiNBo"
	private val homeDir = System.getProperty("user.home") + File.separator + mainDir
	private val homePath = Paths.get(homeDir)

	fun get(): Path {
		if (Files.notExists(homePath))
			Files.createDirectory(homePath)
		return homePath
	}

	fun getOrCreateDefaultConfig(): Path {
		val path = get().resolve(CONFIG_NAME)
		if (Files.notExists(path)) {
			Files.copy(javaClass.getResource("/default-config.yaml").openStream(), path)
		}
		return path
	}

	fun getFile(filePath: Path): Path {
		return checkAndCreate(filePath, { newFile -> Files.createFile(newFile) })
	}

	fun getFileResolved(subPathInTinboDir: String): Path {
		return getFile(get().resolve(subPathInTinboDir))
	}

	private fun checkAndCreate(path: Path, createFile: (Path) -> Path): Path {
		if (Files.notExists(path))
			createFile.invoke(path)
		return path
	}

	fun getDirectory(subPathInTinboDir: String): Path {
		val newDir = get().resolve(subPathInTinboDir)
		return checkAndCreate(newDir, { newDir -> Files.createDirectories(newDir) })
	}

}

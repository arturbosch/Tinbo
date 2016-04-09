package com.gitlab.artismarti.tinbo.config

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author artur
 */
object HomeFolder {

	private val mainDir = "TiNBo"
	private val homeDir = System.getProperty("user.home") + File.separator + mainDir
	private val homePath = Paths.get(homeDir)

	fun get(): Path {
		if (Files.notExists(homePath))
			Files.createDirectory(homePath)
		return homePath
	}

	fun getFile(subPathInTinboDir: Path): Path {
		return checkAndCreate(subPathInTinboDir, { newFile -> Files.createFile(newFile) })
	}

	private fun checkAndCreate(path: Path, createFile: (Path) -> Path): Path {
		if (Files.notExists(path))
			createFile.invoke(path)
		return path
	}

	fun getDirectory(subPathInTinboDir: String): Path {
		var newDir = homePath.resolve(subPathInTinboDir)
		return checkAndCreate(newDir, { newDir -> Files.createDirectories(newDir) })
	}

}


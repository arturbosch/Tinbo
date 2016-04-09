package com.gitlab.artismarti.tinbo.persistence

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.csv.CSVDataExchange
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author artur
 */
abstract class AbstractPersister<E : Entry, D : Data<E>>(private val SAVE_DIR_PATH: Path) {

	private val writer = CSVDataExchange()

	fun store(data: D): Boolean {
		val persist = writer.toCSV(data.entries).joinToString("\n")
		val toSave = HomeFolder.getFile(SAVE_DIR_PATH.resolve(data.name))
		val saved = Files.write(toSave, persist.toByteArray())
		return Files.exists(saved)
	}

	/**
	 * Override this function and call save from abstract persister.
	 */
	abstract fun restore(name: String): D

	protected fun save(name: String, data: D, entryClass: Class<E>): D {
		val path = SAVE_DIR_PATH.resolve(name)
		if (Files.exists(path)) {
			val entriesAsString = Files.readAllLines(path)
			val entries = writer.fromCSV(entryClass, entriesAsString)
			data.entries = entries
		}
		return data
	}
}

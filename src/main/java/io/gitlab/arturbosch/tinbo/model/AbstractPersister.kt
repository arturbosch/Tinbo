package io.gitlab.arturbosch.tinbo.model

import io.gitlab.arturbosch.tinbo.TiNBo
import io.gitlab.arturbosch.tinbo.api.MarkAsPersister
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.csv.CSVDataExchange
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author artur
 */
abstract class AbstractPersister<E : Entry, D : Data<E>>(override val SAVE_DIR_PATH: Path) : MarkAsPersister {

	private val writer = CSVDataExchange()

	fun store(data: D): Boolean {
		val persist = writer.toCSV(data.entries).joinToString("\n")
		val toSave = HomeFolder.getFile(SAVE_DIR_PATH.resolve(data.name))
		val saved = Files.write(toSave, persist.toByteArray())
		return Files.exists(saved)
	}

	/**
	 * Override this function and call load from abstract persister.
	 */
	abstract fun restore(name: String): D

	protected fun load(name: String, data: D, entryClass: Class<E>): D {
		val path = SAVE_DIR_PATH.resolve(name)
		if (Files.exists(path)) {
			val entriesAsString = Files.readAllLines(path)
			val entries = writer.fromCSV(entryClass, entriesAsString)
			data.entries = entries
		}
		TiNBo.config.writeLastUsed(SAVE_DIR_PATH.fileName.toString(), name)
		return data
	}
}

package io.gitlab.arturbosch.tinbo.api.model

import io.gitlab.arturbosch.tinbo.api.marker.PersistableMarker
import io.gitlab.arturbosch.tinbo.api.config.HomeFolder
import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import io.gitlab.arturbosch.tinbo.api.model.util.CSVDataExchange
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
abstract class AbstractPersister<E : Entry, D : Data<E>>(override val persistencePath: Path, val config: TinboConfig) : PersistableMarker {

	private val writer = CSVDataExchange()

	fun store(data: D): Boolean {
		val persist = writer.toCSV(data.entries).joinToString("\n")
		val toSave = HomeFolder.getFile(persistencePath.resolve(data.name))
		val saved = Files.write(toSave, persist.toByteArray())
		return Files.exists(saved)
	}

	/**
	 * Override this function and call load from abstract persister.
	 */
	abstract fun restore(name: String): D

	protected fun load(name: String, data: D, entryClass: Class<E>): D {
		val path = persistencePath.resolve(name)
		if (Files.exists(path)) {
			val entriesAsString = Files.readAllLines(path)
			val entries = writer.fromCSV(entryClass, entriesAsString)
			data.entries = entries
		}
		config.writeLastUsed(persistencePath.fileName.toString(), name)
		return data
	}
}

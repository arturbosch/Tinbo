package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.utils.printlnInfo
import java.util.*

/**
 * @author artur
 */
abstract class EditableCommands<E : Entry, D : Data<E>, T : DummyEntry>(val executor: AbstractExecutor<E, D, T>) {

	private val NEED_EDIT_MODE_TEXT = "Before adding or list entries exit edit mode with 'save' or 'cancel'."

	protected var isListMode: Boolean = false
	protected var isEditMode: Boolean = false

	protected fun parseIndices(indexPattern: String): Set<Int> {
		val result = HashSet<Int>()
		val indices = indexPattern.split(" ")
		val regex = Regex("[1-9][0-9]*")
		val regex2 = Regex("[1-9]+-[0-9]+")
		for (index in indices) {
			if (regex.matches(index)) {
				result.add(index.toInt() - 1)
			} else if (regex2.matches(index)) {
				val interval = index.split("-")
				if (interval.size == 2) {
					val (i1, i2) = Pair(interval[0].toInt(), interval[1].toInt())
					IntRange(i1 - 1, i2 - 1)
							.forEach { result.add(it) }
				} else {
					throw IllegalArgumentException()
				}
			} else {
				throw IllegalArgumentException()
			}
		}
		return result
	}

	protected fun withListMode(body: () -> String): String {
		isListMode = true
		return body.invoke()
	}

	protected fun withinListMode(body: () -> String): String {
		if (isListMode) {
			return body.invoke()
		} else {
			return "Before editing entries you have to 'list' them to get indices to work on."
		}
	}

	protected fun enterEditModeWithIndex(index: Int, body: () -> String): String {
		if (executor.indexExists(index)) {
			isEditMode = true
			return body.invoke()
		} else {
			return "This index doesn't exist"
		}
	}

	protected fun withinEditMode(command: String, body: () -> String): String {
		if (isEditMode) {
			isEditMode = false
			isListMode = false
			return body.invoke()
		} else {
			return "You need to be in edit mode to use $command."
		}
	}

	protected fun whileNotInEditMode(body: () -> String): String {
		if (isEditMode) {
			return NEED_EDIT_MODE_TEXT
		} else {
			return body.invoke()
		}
	}

	fun list(categoryName: String): String {
		return withListMode {
			if (isEditMode) {
				if (categoryName.isNotEmpty())
					printlnInfo("While in edit mode filtering is ignored.")
				executor.listInMemoryEntries()
			} else {
				when (categoryName) {
					"" -> executor.listData()
					else -> executor.listDataFilteredBy(categoryName)
				}
			}
		}
	}

	fun cancel(): String {
		return withinEditMode("cancel") {
			executor.cancel()
			"Cancelled edit mode."
		}
	}

	fun save(name: String): String {
		return withinEditMode("save") {
			executor.saveEntries(name)
			"Successfully saved edited data"
		}
	}

	fun delete(indexPattern: String): String {
		return withinListMode {
			try {
				val indices = parseIndices(indexPattern)
				isEditMode = true
				executor.deleteEntries(indices)
				"Successfully deleted task(s)."
			} catch(e: IllegalArgumentException) {
				"Could not parse the indices pattern. Use something like '1 2 3-5 6'."
			}
		}
	}

}

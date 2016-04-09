package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.HomeFolder
import org.junit.Test

/**
 * @author artur
 */
class TaskDomainTest {

	private val taskData = TaskData("Main")
	private val taskPersister = TaskPersister(HomeFolder.getDirectory("test/tasks"))
	private val taskDataHolder = TaskDataHolder(taskData, taskPersister)

	@Test
	fun domainObjectsTest() {
		val beforeSize = getEntriesSize()
		assert(beforeSize == 0)

		taskDataHolder.persistEntry(TaskEntry("note2"))
		val afterPersistSize = getEntriesSize()
		assert(afterPersistSize == 1)

		taskDataHolder.persistEntry(TaskEntry("note2"))
		val afterSecondPersistSize = getEntriesSize()
		assert(afterSecondPersistSize == 2)

		val isStored = taskPersister.store(taskData)
		assert(isStored)

		taskDataHolder.loadData(taskData.name)
		val totalEntrySizeAfterLoading = getEntriesSize()
		assert(totalEntrySizeAfterLoading == 2)

	}

	private fun getEntriesSize(): Int {
		return taskDataHolder.data.entries.size
	}

}

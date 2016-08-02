package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.common.AbstractExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TaskExecutor @Autowired constructor(taskDataHolder: TaskDataHolder) :
		AbstractExecutor<TaskEntry, TaskData, DummyTask>(taskDataHolder) {

	override val TABLE_HEADER: String
		get() = "No.;Category;Message;Location;Start;End;Description"

	override fun newEntry(index: Int, dummy: DummyTask): TaskEntry {
		val entry = entriesInMemory[index]
		return entry.copy(dummy.message, dummy.description, dummy.location,
				dummy.category, dummy.startTime, dummy.endTime)
	}

}

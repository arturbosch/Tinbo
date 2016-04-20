package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.persistence.AbstractExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class TaskExecutor @Autowired constructor(val taskDataHolder: TaskDataHolder) :
		AbstractExecutor<TaskEntry, TaskData, DummyTask>(taskDataHolder) {

	override val TABLE_HEADER: String
		get() = "No.;Category;Message;Location;Start;End;Description"

	init {
		taskDataHolder.loadData(Default.TASKS_NAME)
	}

	override fun newEntry(index: Int, dummy: DummyTask): TaskEntry {
		val realNote = entriesInMemory[index]
		return TaskEntry(dummy.message.orValue(realNote.message),
				dummy.description.orValue(realNote.description),
				dummy.location.orValue(realNote.location),
				dummy.category.orValue(realNote.category),
				dummy.startTime.orValue(realNote.startTime),
				dummy.endTime.orValue(realNote.endTime))
	}

}

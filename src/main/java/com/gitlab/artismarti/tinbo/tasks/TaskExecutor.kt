package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.persistence.AbstractExecutor
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TaskExecutor(val taskDataHolder: TaskDataHolder = Injekt.get()) :
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

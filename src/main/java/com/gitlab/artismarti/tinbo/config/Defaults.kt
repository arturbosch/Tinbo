package com.gitlab.artismarti.tinbo.config

import com.gitlab.artismarti.tinbo.TiNBo

/**
 * @author artur
 */
object Defaults {

	const val TIME_NAME = "Data"
	const val TASKS_NAME = "Tasks"
	const val NOTES_NAME = "Notes"
	const val FINANCE_NAME = "Finance"
	const val MAIN_CATEGORY_NAME = "Main"
	const val INFO_NOTIFICATION_TIME = 30L

}

object ConfigDefaults {

	const val NOTES = "notes"
	const val TASKS = "tasks"
	const val TIMERS = "timers"
	const val FINANCE = "finance"
	const val LAST_USED = "last-used"
	const val NOTIFICATIONS = "notifications"
	const val TIME_INTERVAL = "interval"
	const val MAIN_CATEGORY_NAME = "category-name"
	const val DEFAULTS = "defaults"
	const val CURRENCY = "currency"

}

val CATEGORY_NAME_DEFAULT = TiNBo.config.getCategoryName()
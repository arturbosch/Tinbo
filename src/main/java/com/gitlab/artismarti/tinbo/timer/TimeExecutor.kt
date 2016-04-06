package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.Notification
import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.csv.CSVTablePrinter
import com.gitlab.artismarti.tinbo.utils.printInfo
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TimeExecutor(val timeDataHolder: TimeDataHolder = Injekt.get()) {

    init {
        timeDataHolder.loadData(Default.DATA_NAME)
    }

    private var currentTimer = Timer.INVALID
    private var running = false

    fun inProgress(): Boolean {
        return !currentTimer.isInvalid()
    }

    private fun listData(data: List<String>): List<String> {
        val csv = CSVTablePrinter()
        val table = data.toMutableList()
        table.add(0, "No.;Category;Date;Hr.;Min;Sec;Notice")
        return csv.asTable(table.toList())
    }

    fun listDataFilterForCategory(categoryName: String): List<String> {
        return listData(timeDataHolder.getEntriesFilteredByCategorySortedByDateAsString(categoryName))
    }

    fun listDataNoFiltering(): List<String> {
        return listData(timeDataHolder.getEntriesSortedByDateAsString())
    }

    fun loadData(name: String) {
        timeDataHolder.loadData(name)
    }

    fun startPrintingTime(timer: Timer) {
        currentTimer = timer
        running = true
        while (running) {
            if (currentTimer.timeMode == TimeMode.DEFAULT)
                printInfo("\rElapsed time: $timer")
            if (currentTimer.isFinished())
                stop()
            Thread.sleep(1000L)
        }
    }

    fun stop(name: String = "", message: String = "") {
        if (inProgress()) {
            running = false
            changeCategoryAndMessageIfNotEmpty(name, message)
            saveAndResetCurrentTimer()
        } else {
            printlnInfo("There is no current timer to stop.")
        }
    }

    private fun changeCategoryAndMessageIfNotEmpty(name: String, message: String) {
        if (name.isEmpty() && message.isEmpty()) return
        var newName = currentTimer.category
        var newMessage = currentTimer.message
        if (name.isNotEmpty()) newName = name
        if (message.isNotEmpty()) newMessage = message
        currentTimer = Timer(currentTimer.timeMode, newName, newMessage,
                currentTimer.startDateTime, currentTimer.stopDateTime)
    }

    private fun saveAndResetCurrentTimer() {
        notify()
        timeDataHolder.persistEntry(createTimeEntry())
        currentTimer = Timer.INVALID
    }

    private fun createTimeEntry(): TimeEntry {
        val (secs, mins, hours) = currentTimer.getTimeTriple()
        return TimeEntry(currentTimer.category, currentTimer.message, hours, mins, secs, currentTimer.startDateTime.toLocalDate())
    }

    private fun notify() {
        Notification.finished(currentTimer.toString())
    }

    fun showTimer(): String {
        if(inProgress()) return "Elapsed time: " + currentTimer.toString()
        else return "No current timer is running"
    }

}

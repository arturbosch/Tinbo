package com.gitlab.artismarti.tinbo.persistence

import com.gitlab.artismarti.tinbo.timer.TimerCategory

/**
 * @author artur
 */
abstract class Data(val name: String) {

    private var categories = listOf<Category>()

    private fun addCategory(category: Category) {
        categories = categories.plus(category)
    }

    fun addForCategory(categoryName: String, entry: Entry) {
        val category = categories.find { it.name == categoryName }
        category?.addEntry(entry) ?: addCategory(TimerCategory(categoryName).apply { addEntry(entry) })
    }

    override fun toString(): String{
        return "Data(name='$name', categories=$categories)"
    }

}

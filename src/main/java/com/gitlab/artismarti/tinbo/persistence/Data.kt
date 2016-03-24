package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
abstract class Data {

    val name = ""
    private var categories = listOf<Category>()

    fun addEntry(category: Category) {
        categories = categories.plus(category)
    }

    fun getCategories() = categories
}

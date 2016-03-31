package com.gitlab.artismarti.tinbo

import java.util.ArrayList

/**
 * @author artur
 */
fun String.spaceIfEmpty(): String =
        when (this) {
            "" -> " "
            else -> this
        }

fun <E> List<E>.plusElementAtBeginning(element: E): List<E> {
    val result = ArrayList<E>(size + 1)
    result.add(element)
    result.addAll(this)
    return result
}

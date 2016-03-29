package com.gitlab.artismarti.tinbo

/**
 * @author artur
 */
fun String.spaceIfEmpty(): String =
        when (this) {
            "" -> " "
            else -> this
        }

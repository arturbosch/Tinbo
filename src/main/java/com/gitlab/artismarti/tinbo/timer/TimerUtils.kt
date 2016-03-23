package com.gitlab.artismarti.tinbo.timer

/**
 * @author artur
 */

/**
 * Transforms a long number into a string by calling the to string method specified
 * by the format 'xx' where xx can't be higher than 59.
 */
fun Long.toNumberString(): String {
    return this.mod(60).toString().apply {
        if (this.length == 1) {
            return "0$this"
        }
    }
}

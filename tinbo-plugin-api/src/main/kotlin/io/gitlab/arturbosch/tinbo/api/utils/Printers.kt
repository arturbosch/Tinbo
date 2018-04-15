package io.gitlab.arturbosch.tinbo.api.utils

import org.fusesource.jansi.Ansi

/**
 * @author Artur Bosch
 */

fun printInfo(message: String) {
	print(Ansi.ansi().fg(Ansi.Color.BLACK).bg(Ansi.Color.WHITE).a(message).reset())
}

fun printlnInfo(message: String) {
	printInfo(message)
	println()
}

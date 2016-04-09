package com.gitlab.artismarti.tinbo.utils

import org.fusesource.jansi.Ansi

/**
 * @author artur
 */

fun printInfo(message: String) {
	print(Ansi.ansi().fg(Ansi.Color.BLACK).bg(Ansi.Color.WHITE).a(message).reset())
}

fun printlnInfo(message: String) {
	printInfo(message)
	println()
}

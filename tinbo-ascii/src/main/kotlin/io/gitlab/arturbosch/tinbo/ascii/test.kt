package io.gitlab.arturbosch.tinbo.ascii

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val run = Ascii().run("/home/artur/Pictures/handy/IMG_20150606_162753.jpg", false)
	System.out.print(run)
}
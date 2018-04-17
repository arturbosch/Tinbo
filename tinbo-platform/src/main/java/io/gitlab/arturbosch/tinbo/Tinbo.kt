package io.gitlab.arturbosch.tinbo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author artur
 */
@SpringBootApplication
open class Tinbo

fun main(args: Array<String>) {
	SpringApplication.run(Tinbo::class.java, *args)
}

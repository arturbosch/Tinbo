package com.gitlab.artismarti.tinbo.common

/**
 * @author artur
 */
interface Summarizable {
	fun sum(categories: List<String>): String
}
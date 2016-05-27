package com.gitlab.artismarti.tinbo.config

import org.yaml.snakeyaml.Yaml
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * @author artur
 */
class TinboConfig private constructor(private val values: Map<String, Map<String, String>>) {

	fun getKey(key: String): Map<String, String> {
		return values.getOrElse(key, { HashMap<String, String>() })
	}

	companion object {

		@Suppress("UNCHECKED_CAST")
		@Throws(IOException::class)
		fun load(path: Path): TinboConfig {
			return Files.newBufferedReader(path).use {
				val yaml = Yaml()
				val config = yaml.loadAs(it, Map::class.java) as Map<String, Map<String, String>>
				TinboConfig(config)
			}
		}
	}
}

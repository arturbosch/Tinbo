package io.gitlab.arturbosch.tinbo.config

import io.gitlab.arturbosch.tinbo.orDefault
import io.gitlab.arturbosch.tinbo.toLongOrDefault
import org.yaml.snakeyaml.Yaml
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.HashMap

/**
 * @author artur
 */
class TinboConfig private constructor(private var values: Map<String, Map<String, String>>) {

	fun getKey(key: String): Map<String, String> {
		return values.getOrElse(key, { HashMap<String, String>() })
	}

	fun writeLastUsed(property: String, value: String) {
		val oldMap = values[property]
		if (oldMap != null) {
			if (oldMap[ConfigDefaults.LAST_USED] != value) {
				val updatedMap = oldMap.plus(ConfigDefaults.LAST_USED to value)
				values = values.plus(property to updatedMap)
				TinboConfig.write(HomeFolder.getOrCreateDefaultConfig(), this)
			}
		}
	}

	fun getTimeInterval(): Long {
		val time = getKey(ConfigDefaults.NOTIFICATIONS)[ConfigDefaults.TIME_INTERVAL].orEmpty()
		return time.toLongOrDefault({ Defaults.INFO_NOTIFICATION_TIME })
	}

	fun getCategoryName(): String {
		return getKey(ConfigDefaults.DEFAULTS)[ConfigDefaults.MAIN_CATEGORY_NAME]
				.orDefault(Defaults.MAIN_CATEGORY_NAME)
	}

	fun getCity(): String {
		return getKey(ConfigDefaults.DEFAULTS)[ConfigDefaults.CITY]
				.orDefault("-1")
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

		fun write(path: Path, config: TinboConfig): Boolean {
			val dumpAsMap = Yaml().dumpAsMap(config.values)
			return Files.write(path, dumpAsMap.toByteArray(), StandardOpenOption.CREATE).run {
				Files.exists(this)
			}
		}

	}
}

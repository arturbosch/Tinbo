package io.gitlab.arturbosch.tinbo.api.config

import io.gitlab.arturbosch.tinbo.api.orDefault
import io.gitlab.arturbosch.tinbo.api.toIntOrDefault
import io.gitlab.arturbosch.tinbo.api.toLongOrDefault
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.HashMap

/**
 * @author Artur Bosch
 */
@Component
class TinboConfig(val path: Path = HomeFolder.getOrCreateDefaultConfig()) {

	private var values: Map<String, Map<String, String>>

	init {
		values = Files.newBufferedReader(path).use {
			@Suppress("UNCHECKED_CAST")
			Yaml().loadAs(it, Map::class.java) as Map<String, Map<String, String>>
		}
	}

	fun getKey(key: String): Map<String, String> {
		return values.getOrElse(key, { HashMap() })
	}

	fun getKeySafe(key: String): Map<String, Any> = values.getOrElse(key, { HashMap<String, Any>() })

	fun writeLastUsed(property: String, value: String) {
		val oldMap = values[property]
		if (oldMap != null) {
			if (oldMap[ConfigDefaults.LAST_USED] != value) {
				val updatedMap = oldMap.plus(ConfigDefaults.LAST_USED to value)
				values = values.plus(property to updatedMap)
				writeToFile()
			}
		}
	}

	fun writeToFile(): Boolean {
		val dumpAsMap = Yaml().dumpAsMap(values)
		return Files.write(path, dumpAsMap.toByteArray(), StandardOpenOption.CREATE).run {
			Files.exists(this)
		}
	}

	fun getTimeInterval(): Long {
		val time = getKeySafe(ConfigDefaults.NOTIFICATIONS)[ConfigDefaults.TIME_INTERVAL].toString()
		return time.toLongOrDefault({ Defaults.INFO_NOTIFICATION_TIME })
	}

	fun getCategoryName(): String {
		return getKey(ConfigDefaults.DEFAULTS)[ConfigDefaults.MAIN_CATEGORY_NAME]
				.orDefault(Defaults.MAIN_CATEGORY_NAME)
	}

	fun getCity(): String = getKey(ConfigDefaults.DEFAULTS)[ConfigDefaults.CITY].orDefault("-1")

	fun getListAmount(): Int {
		val amount = getKeySafe(ConfigDefaults.DEFAULTS)[ConfigDefaults.LIST_AMOUNT].toString()
		return amount.toIntOrDefault { Defaults.LIST_ENTRIES }
	}

}

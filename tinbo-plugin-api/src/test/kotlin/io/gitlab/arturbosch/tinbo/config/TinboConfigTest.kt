package io.gitlab.arturbosch.tinbo.config

import com.google.common.io.Resources
import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import org.junit.After
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author artur
 */
class TinboConfigTest {

	private val path = Files.createTempFile("tinbo", "config")

	private val config: TinboConfig =
			TinboConfig(Paths.get(
					Resources.getResource("default-config.yaml").toURI()))

	@After
	fun tearDown() {
		Files.deleteIfExists(path)
	}

	@Test
	fun writeConfig() {
		val write = config.writeToFile(path)
		assert(write)
	}

	@Test
	fun putValues() {
		config.put("kanbin", mapOf("lastUsed" to "test"))
		assert(config.getKey("kanbin").containsValue("test"))
	}

	@Test
	fun timeInterval() {
		val timeInterval = config.getTimeInterval()
		assert(timeInterval == 15L)
	}

	@Test
	fun getCategoryName() {
		val name = config.getCategoryName()
		assert(name == "Main")
	}
}

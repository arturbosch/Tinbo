package io.gitlab.arturbosch.tinbo.config

import com.google.common.io.Resources
import org.junit.After
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author artur
 */
class TinboConfigTest {

	private val path = Paths.get("test")

	private val config: TinboConfig
		get() {
			val tinboConfig = TinboConfig(
					Paths.get(Resources.getResource("default-config.yaml").file))
			return tinboConfig
		}

	@After
	fun tearDown() {
		Files.deleteIfExists(path)
	}

	@Test
	fun writeConfig() {
		val write = config.writeToFile()
		assert(write)
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

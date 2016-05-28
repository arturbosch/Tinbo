package com.gitlab.artismarti.tinbo.config

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

	@After
	fun tearDown() {
		if (Files.exists(path))
			Files.delete(path)
	}

	@Test
	fun test() {
		val tinboConfig = TinboConfig.load(
				Paths.get(Resources.getResource("default-config.yaml").file))
		val write = TinboConfig.write(path, tinboConfig)

		assert(write)
	}
}

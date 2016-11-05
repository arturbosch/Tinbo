package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.config.TinboConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Suppress("unused")
@Component
class SpringContext @Autowired constructor(val context: ApplicationContext,
										   val helpers: List<PluginHelper>,
										   val tinboConfig: TinboConfig)
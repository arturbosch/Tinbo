package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.config.TinboConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class SpringContext @Autowired constructor(val context: ApplicationContext,
										   val helpers: List<PluginHelper>,
										   val tinboConfig: TinboConfig) {

	fun registerBeanDefinition(name: String, bean: BeanDefinition) {
		(context as GenericApplicationContext).defaultListableBeanFactory
				.registerBeanDefinition(name, bean)
	}

	fun registerSingleton(name: String, obj: Any) {
		(context as GenericApplicationContext).beanFactory.registerSingleton(name, obj)
	}
}
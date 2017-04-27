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
class TinboContext @Autowired constructor(val context: ApplicationContext,
										  val tinboConfig: TinboConfig) {

	private val _helpers = lazy { context.getBeansOfType(PluginHelper::class.java).values }

	@PluginSupport
	fun pluginHelpers() = _helpers.value

	@PluginSupport
	fun registerBeanDefinition(name: String, bean: BeanDefinition) {
		(context as GenericApplicationContext).defaultListableBeanFactory
				.registerBeanDefinition(name, bean)
	}

	@PluginSupport
	fun registerSingleton(name: String, obj: Any) {
		(context as GenericApplicationContext).beanFactory.registerSingleton(name, obj)
	}

	@PluginSupport
	inline fun <reified T : Any> beanOf(): T {
		return context.getBean(T::class.java)
	}
}
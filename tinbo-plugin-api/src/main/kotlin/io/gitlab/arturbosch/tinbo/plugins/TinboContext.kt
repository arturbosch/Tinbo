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
	fun registerSingletons(objects: List<Any>) {
		for (obj in objects) {
			registerSingleton(obj::class.java.simpleName, obj)
		}
	}

	@PluginSupport
	fun <T> beanOf(clazz: Class<T>): T = context.getBean(clazz)

	@PluginSupport
	inline fun <reified T : Any> beanOf(): T = beanOf(T::class.java)

	@PluginSupport
	fun <T> beansOf(clazz: Class<T>): MutableMap<String, T> = context.getBeansOfType(clazz)

	@PluginSupport
	inline fun <reified T : Any> beansOf(): MutableMap<String, T> = beansOf(T::class.java)
}

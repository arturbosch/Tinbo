package io.gitlab.arturbosch.tinbo.api.plugins

import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TinboContext @Autowired constructor(val context: GenericApplicationContext,
										  val config: TinboConfig) {
	@PluginSupport
	val helpers by lazy { context.getBeansOfType(PluginHelper::class.java).values }

	@PluginSupport
	@Deprecated("Use property helpers instead", ReplaceWith("helpers"))
	fun pluginHelpers() = helpers

	@PluginSupport
	fun registerBeanDefinition(name: String, bean: BeanDefinition) {
		context.defaultListableBeanFactory.registerBeanDefinition(name, bean)
	}

	@PluginSupport
	fun registerSingleton(obj: Any) {
		context.beanFactory.registerSingleton(obj.javaClass.name, obj)
	}

	@PluginSupport
	@Deprecated("Do not provide a bean name on your own.", ReplaceWith("registerSingleton(obj)"))
	fun registerSingleton(name: String, obj: Any) {
		context.beanFactory.registerSingleton(name, obj)
	}

	@PluginSupport
	fun registerSingletons(objects: List<Any>) {
		for (obj in objects) {
			registerSingleton(obj)
		}
	}

	@PluginSupport
	fun hasBean(any: Any): Boolean = context.containsBean(any.javaClass.name)

	@PluginSupport
	fun hasBean(name: String): Boolean = context.containsBean(name)

	@PluginSupport
	fun <T> beanOf(clazz: Class<T>): T = context.getBean(clazz)

	@PluginSupport
	inline fun <reified T : Any> beanOf(): T = beanOf(T::class.java)

	@PluginSupport
	fun <T> beansOf(clazz: Class<T>): MutableMap<String, T> = context.getBeansOfType(clazz)

	@PluginSupport
	inline fun <reified T : Any> beansOf(): MutableMap<String, T> = beansOf(T::class.java)
}

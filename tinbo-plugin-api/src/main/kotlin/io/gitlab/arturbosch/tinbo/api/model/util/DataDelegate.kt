package io.gitlab.arturbosch.tinbo.api.model.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Artur Bosch
 */
private class DataDelegate<T>(private var initializer: () -> T) : ReadWriteProperty<Any?, T> {

	private var value: T? = null

	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
		if (value == null) {
			synchronized(this) {
				value = initializer()
			}
		}
		return value!!
	}

	override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		this.value = value
	}
}

fun <T> lazyData(initializer: () -> T): ReadWriteProperty<Any?, T> = DataDelegate(initializer)

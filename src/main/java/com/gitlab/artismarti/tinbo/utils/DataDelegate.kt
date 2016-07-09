package com.gitlab.artismarti.tinbo.utils

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author artur
 */
private class DataDelegate<T>(private var initializer: () -> T) : ReadWriteProperty<Any?, T> {

	private var value: T? = null

	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
		if (value == null) {
			value = initializer()
		}
		return value!!
	}

	override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		this.value = value
	}
}

object DelegateExt {
	fun <T> lazyData(initializer: () -> T): ReadWriteProperty<Any?, T> = DataDelegate(initializer)
}

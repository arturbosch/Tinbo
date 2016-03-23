package com.gitlab.artismarti.tinbo.providers

import org.springframework.core.annotation.Order
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
class HistoryProvider : DefaultHistoryFileNameProvider() {

    override fun getProviderName(): String? {
        return "TinboHistory"
    }

    override fun getHistoryFileName(): String? {
        return "TinboShellHistory"
    }
}
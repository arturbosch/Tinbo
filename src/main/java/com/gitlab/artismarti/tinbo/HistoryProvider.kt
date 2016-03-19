package com.gitlab.artismarti.tinbo

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
        return "My History"
    }

    override fun getHistoryFileName(): String? {
        return "history"
    }
}

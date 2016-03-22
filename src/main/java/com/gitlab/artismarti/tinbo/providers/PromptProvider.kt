package com.gitlab.artismarti.tinbo.providers

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.shell.plugin.support.DefaultPromptProvider
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
class PromptProvider : DefaultPromptProvider() {

    override fun getProviderName(): String {
        return "TinboPromptProvider"
    }

    override fun getPrompt(): String {
        return "tinbo>"
    }
}

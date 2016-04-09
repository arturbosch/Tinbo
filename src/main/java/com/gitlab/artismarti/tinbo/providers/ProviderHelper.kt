package com.gitlab.artismarti.tinbo.providers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class ProviderHelper @Autowired constructor(val promptProvider: PromptProvider,
											val bannerProvider: BannerProvider) {

	fun changePrompt(value: String) = {
		promptProvider.promptText = value
	}

	fun getWelcome(): String = "\n${bannerProvider.banner}\n${bannerProvider.welcomeMessage}"
}

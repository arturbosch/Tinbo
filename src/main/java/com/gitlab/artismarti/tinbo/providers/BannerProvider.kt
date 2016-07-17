package com.gitlab.artismarti.tinbo.providers

import com.google.common.io.Resources
import org.springframework.core.annotation.Order
import org.springframework.shell.plugin.support.DefaultBannerProvider
import org.springframework.shell.support.util.OsUtils
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
open class BannerProvider : DefaultBannerProvider() {

	override fun getBanner(): String {
		return Resources.readLines(Resources.getResource("banner.txt"),
				Charsets.UTF_8).joinToString(separator = OsUtils.LINE_SEPARATOR)
	}

	override fun getVersion(): String {
		return "TiNBo v1.0"
	}

	override fun getWelcomeMessage(): String {
		return "Welcome to TiNBo. Use 'help' to view all commands!"
	}

	override fun getProviderName(): String {
		return "Tinbo"
	}
}

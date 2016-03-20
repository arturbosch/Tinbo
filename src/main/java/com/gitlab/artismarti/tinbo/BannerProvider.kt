package com.gitlab.artismarti.tinbo

import org.springframework.core.annotation.Order
import org.springframework.shell.plugin.support.DefaultBannerProvider
import org.springframework.shell.support.util.OsUtils
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
class BannerProvider : DefaultBannerProvider() {

    override fun getBanner(): String {
        return "=======================================" + OsUtils.LINE_SEPARATOR +
                "*                                     *" + OsUtils.LINE_SEPARATOR +
                "*            HelloWorld               *" + OsUtils.LINE_SEPARATOR +
                "*                                     *" + OsUtils.LINE_SEPARATOR +
                "=======================================" + OsUtils.LINE_SEPARATOR +
                "Version:" + this.version
    }

    override fun getVersion(): String {
        return "TiNBo v0.1"
    }

    override fun getWelcomeMessage(): String {
        return "Welcome to TiNBo. Use 'help' to view all commands!"
    }

    override fun getProviderName(): String {
        return "TinboBanner"
    }
}

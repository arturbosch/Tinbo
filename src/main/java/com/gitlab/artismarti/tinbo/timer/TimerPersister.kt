package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.BasePersister
import com.gitlab.artismarti.tinbo.persistence.Data

/**
 * @author artur
 */
class TimerPersister : BasePersister() {

    override fun store(data: Data): Boolean {
        throw UnsupportedOperationException()
    }

    override fun restore(name: String): String {
        throw UnsupportedOperationException()
    }
}

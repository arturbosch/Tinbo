package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.Entry

/**
 * @author artur
 */
class Note : Entry() {

    override fun compareTo(other: Entry): Int {
        throw UnsupportedOperationException()
    }

}

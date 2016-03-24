package com.gitlab.artismarti.tinbo.timer

import com.gitlab.artismarti.tinbo.persistence.Entry

/**
 * @author artur
 */
class TimerEntry(val message: String = "invalid",
                 val hours: Int = -1,
                 val minutes: Int = -1,
                 val seconds: Int = -1) : Entry() {


}

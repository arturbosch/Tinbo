package com.gitlab.artismarti.tinbo.notes

import org.springframework.stereotype.Component
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
@Component
class NotesCommands(val executor: NotesExecutor = Injekt.get()) {


}

package com.gitlab.artismarti.tinbo.commands

import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class HelloCommand : CommandMarker {

    @CliCommand(value = "hello", help = "Prints a hello message for given name.")
    fun hello(@CliOption(key = arrayOf("name"), mandatory = false, specifiedDefaultValue = "World",
            unspecifiedDefaultValue = "World", help = "Which name to greet?") name: String = "World"): String {

        return "Hello $name";
    }
}

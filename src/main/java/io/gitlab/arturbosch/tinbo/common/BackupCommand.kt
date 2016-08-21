package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class BackupCommand : Command {
	override val id: String = "share"

	@CliCommand("backup local", help = "Stores all data sets into the backup folder.")
	fun backupLocal(): String {
		return "Backup'ed data into local backup folder."
	}

	@CliCommand("backup remote", help = "Uploads all data sets to your specified TiNBo server.")
	fun backupRemote(@CliOption(key = arrayOf("", "url"),
			specifiedDefaultValue = "",
			unspecifiedDefaultValue = "") url: String): String {
		return "Backup'ed data into remote tinbo server: $url."
	}

}
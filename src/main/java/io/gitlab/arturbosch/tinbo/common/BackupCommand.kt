package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.MarkAsPersister
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.util.Comparator

/**
 * @author Artur Bosch
 */
@Component
class BackupCommand @Autowired constructor(val persisters: List<MarkAsPersister>) : Command {
	override val id: String = "share"

	@CliCommand("backup local", help = "Stores all data sets into the backup folder.")
	fun backupLocal(): String {
		val backupDir = HomeFolder.getDirectory(HomeFolder.BACKUP)
		cleanBackupDir(backupDir)
		backupModes(backupDir)
		return "Backup'ed data into local backup folder."
	}

	private fun cleanBackupDir(backupDir: Path) {
		Files.walk(backupDir, FileVisitOption.FOLLOW_LINKS)
				.sorted(Comparator.reverseOrder())
				.forEach { if (it != backupDir) Files.deleteIfExists(it) }
	}

	private fun backupModes(backupDir: Path) {
		persisters.map { it.SAVE_DIR_PATH }
				.forEach { pathToMode -> copyFilesFromMode(backupDir, pathToMode) }
	}

	private fun copyFilesFromMode(backupDir: Path, pathToMode: Path) {
		val modePathInBackUp = backupDir.resolve(pathToMode.fileName)
		Files.copy(pathToMode, modePathInBackUp)
		Files.walk(pathToMode).forEach {
			if (it != pathToMode)
				Files.copy(it, modePathInBackUp.resolve(it.fileName))
		}
	}

	@CliCommand("backup remote", help = "Uploads all data sets to your specified TiNBo server.")
	fun backupRemote(@CliOption(key = arrayOf("", "url"),
			specifiedDefaultValue = "",
			unspecifiedDefaultValue = "") url: String): String {
		return "Backup'ed data to remote tinbo server: $url."
	}

}
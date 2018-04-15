package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.PersistableMarker
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.config.TinboConfig
import io.gitlab.arturbosch.tinbo.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.utils.HttpClient
import io.gitlab.arturbosch.tinbo.utils.dateTimeFormatter
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.Base64
import java.util.Comparator
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * @author Artur Bosch
 */
@Component
class BackupCommand @Autowired constructor(
		private val tinboContext: TinboContext,
		private val config: TinboConfig) : Command {

	override val id: String = "share"

	private val logger = LogManager.getLogger(javaClass)

	private var _persisters = lazy {
		tinboContext.context
				.getBeansOfType(PersistableMarker::class.java)
				.values.toList()
	}

	private fun persisters() = _persisters.value

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
		persisters().map { it.persistencePath }
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
	fun backupRemote(@CliOption(key = ["", "url"],
			specifiedDefaultValue = "",
			unspecifiedDefaultValue = "") url: String) {

		backupLocal()

		val config = config.getKey("remote-backup")
		val backupName = config.getOrElse("name", { "backup" }) + "-" +
				dateTimeFormatter.format(LocalDateTime.now())
		val backupServer =
				if (url.isEmpty()) {
					config["server"] ?: throw BackUpServerError("No server url specified in tinbo config!")
				} else url
		val backupCredentials = config["credentials"]
				?: throw BackUpServerError("No credentials specified in tinbo config!")

		val zippedBackup = zipBackup(backupName)
		uploadZip(backupCredentials, backupServer, zippedBackup)
		Files.deleteIfExists(zippedBackup)
	}

	private fun uploadZip(backupCredentials: String, backupServer: String, zippedBackup: Path) {
		val encoded = "Basic " + Base64.getEncoder()
				.encode(backupCredentials.toByteArray())
				.toString(Charsets.ISO_8859_1)
		val url = "$backupServer/backup"
		val headers = mapOf("Authorization" to encoded)
		try {
			val response = HttpClient(url, "UTF-8", headers)
					.addFilePart(zippedBackup.fileName.toString(), zippedBackup)
					.execute()
			if (response.failure()) {
				val error = response.errorBody()
				printlnInfo("${error.message} (${response.status})")
				logger.error(error.message, error)
			} else {
				printlnInfo("Successfully backup'ed data (${response.status}).")
			}
		} catch (ex: IOException) {
			val message = "Could not establish connection to tinbo server."
			printlnInfo(message)
			logger.error(message, ex)
		}
	}

	private fun zipBackup(backupName: String): Path {
		val backupDir = HomeFolder.getDirectory(HomeFolder.BACKUP).toAbsolutePath()
		val zipped = HomeFolder.getFileResolved("$backupName.zip")

		with(ZipOutputStream(Files.newOutputStream(zipped))) {
			Files.walk(backupDir)
					.filter { !Files.isDirectory(it) }
					.forEach {
						putNextEntry(ZipEntry(it.fileName.toString()))
						Files.copy(it, this)
						closeEntry()
					}
			close()
		}
		return zipped
	}

	private class BackUpServerError(message: String) : RuntimeException(message)
}

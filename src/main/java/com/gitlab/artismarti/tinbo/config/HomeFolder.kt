package com.gitlab.artismarti.tinbo.config

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author artur
 */
object HomeFolder {

    private val notesDir = "TiNBo"
    private val homeDir = System.getProperty("user.home") + File.separator + notesDir
    private val homePath = Paths.get(homeDir)

    fun get(): Path {
        if (Files.notExists(homePath))
            Files.createDirectory(homePath)
        return homePath
    }

    fun newOrGetFile(subPathInKNotesDir: String): Path {
        var newFile = homePath.resolve(subPathInKNotesDir)
        if (Files.notExists(newFile))
            newFile = Files.createFile(newFile)
        return newFile
    }

    fun fileExists(fileName: String): Boolean = Files.exists(homePath.resolve(fileName))
}


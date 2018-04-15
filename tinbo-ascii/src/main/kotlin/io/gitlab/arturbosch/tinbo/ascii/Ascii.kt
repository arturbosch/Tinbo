package io.gitlab.arturbosch.tinbo.ascii

import io.gitlab.arturbosch.tinbo.api.marker.Command
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * @author Artur Bosch
 */
class Ascii : Command {

	override val id: String = "plugins"

	@CliCommand("plugin ascii", help = "Converts an given image to ascii art.")
	fun run(@CliOption(key = ["", "path"]) path: String?,
			@CliOption(key = ["invert"], unspecifiedDefaultValue = "false",
					specifiedDefaultValue = "true") invert: Boolean): String {
		return path?.let {
			val path1 = Paths.get(path)
			if (Files.notExists(path1)) return "Specified path does not exist!"
			if (!isImage(path1)) return "Given path points not to an image (jpg or png)."
			val image = ImageIO.read(path1.toFile())
			val resizeImage = resizeImage(image, 76, 0)
			return ASCII(invert).convert(resizeImage)
		} ?: "Provided path does not exist"
	}

	private fun isImage(path: Path): Boolean {
		val stringPath = path.toString().toLowerCase()
		fun endsWith(sub: String): Boolean = stringPath.endsWith(sub)
		return endsWith("jpg") || endsWith("jpeg") || endsWith("png")
	}

	private fun resizeImage(image: BufferedImage, width: Int, height: Int): BufferedImage {
		var cWidth = width
		var cHeight = height
		if (cWidth < 1) {
			cWidth = 1
		}
		if (cHeight <= 0) {
			val aspectRatio = cWidth.toDouble() / image.width * 0.5
			cHeight = Math.ceil(image.height * aspectRatio).toInt()
		}
		val resized = BufferedImage(cWidth, cHeight, BufferedImage.TYPE_INT_RGB)
		val scaled = image.getScaledInstance(cWidth, cHeight, Image.SCALE_DEFAULT)
		resized.graphics.drawImage(scaled, 0, 0, null)
		return resized
	}

	class ASCII(private val negative: Boolean = false) {

		fun convert(image: BufferedImage): String {
			val sb = StringBuilder((image.width + 1) * image.height)
			for (y in 0 until image.height) {
				if (sb.isNotEmpty()) sb.append("\n")
				(0 until image.width)
						.asSequence()
						.map { Color(image.getRGB(it, y)) }
						.map { it.red.toDouble() * 0.30 + it.blue.toDouble() * 0.59 + it.green.toDouble() * 0.11 }
						.map { if (negative) returnStrNeg(it) else returnStrPos(it) }
						.forEach { sb.append(it) }
			}
			return sb.toString()
		}

		private fun returnStrPos(g: Double) = when {
			g >= 230.0 -> ' '
			g >= 200.0 -> '.'
			g >= 180.0 -> '*'
			g >= 160.0 -> ':'
			g >= 130.0 -> 'o'
			g >= 100.0 -> '&'
			g >= 70.0 -> '8'
			g >= 50.0 -> '#'
			else -> '@'
		}

		private fun returnStrNeg(g: Double) = when {
			g >= 230.0 -> '@'
			g >= 200.0 -> '#'
			g >= 180.0 -> '8'
			g >= 160.0 -> '&'
			g >= 130.0 -> 'o'
			g >= 100.0 -> ':'
			g >= 70.0 -> '*'
			g >= 50.0 -> '.'
			else -> ' '
		}
	}
}

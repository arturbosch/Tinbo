package io.gitlab.arturbosch.tinbo

import jline.console.ConsoleReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.shell.CommandLine
import org.springframework.shell.converters.ArrayConverter
import org.springframework.shell.converters.AvailableCommandsConverter
import org.springframework.shell.converters.BigDecimalConverter
import org.springframework.shell.converters.BigIntegerConverter
import org.springframework.shell.converters.BooleanConverter
import org.springframework.shell.converters.CharacterConverter
import org.springframework.shell.converters.DateConverter
import org.springframework.shell.converters.DoubleConverter
import org.springframework.shell.converters.EnumConverter
import org.springframework.shell.converters.FloatConverter
import org.springframework.shell.converters.IntegerConverter
import org.springframework.shell.converters.LocaleConverter
import org.springframework.shell.converters.LongConverter
import org.springframework.shell.converters.ShortConverter
import org.springframework.shell.converters.SimpleFileConverter
import org.springframework.shell.converters.StaticFieldConverterImpl
import org.springframework.shell.converters.StringConverter
import org.springframework.shell.core.JLineShellComponent

/**
 * @author artur
 */
@Configuration
open class ShellConfiguration {

	@Bean open fun commandLine(): CommandLine {
		return CommandLine(null, 3000, null)
	}

	@Bean open fun console(): ConsoleReader {
		return ConsoleReader()
	}

	@Bean open fun shell(): JLineShellComponent {
		val shell = JLineShellComponent()
		val simpleParser = shell.simpleParser
		simpleParser.add(StringConverter())
		simpleParser.add(IntegerConverter())
		simpleParser.add(BooleanConverter())
		simpleParser.add(LongConverter())
		simpleParser.add(SimpleFileConverter())
		simpleParser.add(DoubleConverter())
		simpleParser.add(AvailableCommandsConverter())
		simpleParser.add(ArrayConverter())
		simpleParser.add(BigDecimalConverter())
		simpleParser.add(BigIntegerConverter())
		simpleParser.add(CharacterConverter())
		simpleParser.add(DateConverter())
		simpleParser.add(EnumConverter())
		simpleParser.add(FloatConverter())
		simpleParser.add(ShortConverter())
		simpleParser.add(StaticFieldConverterImpl())
		simpleParser.add(LocaleConverter())
		return shell
	}
}
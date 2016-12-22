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
import org.springframework.shell.core.SimpleParser

/**
 * @author Artur Bosch
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
		return JLineShellComponent().apply {
			simpleParser.addPrimitiveConverters()
			simpleParser.addObjectConverters()
		}
	}

	private fun SimpleParser.addPrimitiveConverters() {
		add(StringConverter())
		add(IntegerConverter())
		add(BooleanConverter())
		add(LongConverter())
		add(CharacterConverter())
		add(FloatConverter())
		add(ShortConverter())
		add(DoubleConverter())
	}

	private fun SimpleParser.addObjectConverters() {
		add(ArrayConverter())
		add(SimpleFileConverter())
		add(AvailableCommandsConverter())
		add(BigDecimalConverter())
		add(BigIntegerConverter())
		add(DateConverter())
		add(EnumConverter())
		add(StaticFieldConverterImpl())
		add(LocaleConverter())
	}
}
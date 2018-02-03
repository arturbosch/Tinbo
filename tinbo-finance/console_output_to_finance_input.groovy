import java.nio.file.Files
import java.nio.file.Paths
import java.time.format.DateTimeFormatter

base = "/home/artur/Tinbo/finance/"
file = "${base}Finance__"
save = "${base}Recovered"

before_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
after_formatter = DateTimeFormatter.ISO_DATE_TIME

def recoverDateTime(String line) {
	splitted = line.split(";")
	date = splitted[-1]
	dateTime = before_formatter.parse(date)
	formatted = after_formatter.format(dateTime)
	return splitted[0..-2].join(";") + ";$formatted"
}

result = Files.lines(Paths.get(file))
		.filter { !it.isEmpty() }
		.map { it.substring(it.indexOf("|") + 1) }
		.map { it.replace("|", ";") }
		.map { it.split(";").collect { it.trim() }.join(";") }
		.map { recoverDateTime(it) }
		.collect()

Files.write(Paths.get(save), result)

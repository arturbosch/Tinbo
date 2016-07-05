package com.gitlab.artismarti.tinbo.common;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.MethodTarget;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.support.util.NaturalOrderComparator;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Extraction of some methods of SimpleParser by Ben Alex. Modified to meet my requirement
 * to show only the help for commands of current mode.
 *
 * @author Ben Alex
 * @author artur
 */
class HelpParser {

	private static final Comparator<Object> COMPARATOR = new NaturalOrderComparator<>();

	private List<CommandMarker> commands;

	public HelpParser(List<CommandMarker> commands) {
		this.commands = commands;
	}

	public String obtainHelp(
			@CliOption(key = {"", "command"}, optionContext = "availableCommands", help = "Command name to provide help for") String buffer) {

		if (buffer == null) {
			buffer = "";
		}

		StringBuilder sb = new StringBuilder();

		// Figure out if there's a single command we can offer help for
		Collection<MethodTarget> matchingTargets = locateTargets(buffer, false);
		for (MethodTarget candidate : matchingTargets) {
			if (buffer.equals(candidate.getKey())) {
				matchingTargets = Collections.singleton(candidate);
				break;
			}
		}
		if (matchingTargets.size() == 1) {
			// Single command help
			MethodTarget methodTarget = matchingTargets.iterator().next();

			// Argument conversion time
			Annotation[][] parameterAnnotations = methodTarget.getMethod().getParameterAnnotations();
			if (parameterAnnotations.length > 0) {
				// Offer specified help
				CliCommand cmd = methodTarget.getMethod().getAnnotation(CliCommand.class);
				Assert.notNull(cmd, "CliCommand not found");

				for (String value : cmd.value()) {
					sb.append("Keyword:                   ").append(value).append(OsUtils.LINE_SEPARATOR);
				}

				sb.append("Description:               ").append(cmd.help()).append(OsUtils.LINE_SEPARATOR);

				for (Annotation[] annotations : parameterAnnotations) {
					CliOption cliOption = null;
					for (Annotation a : annotations) {
						if (a instanceof CliOption) {
							cliOption = (CliOption) a;

							for (String key : cliOption.key()) {
								if ("".equals(key)) {
									key = "** default **";
								}
								sb.append(" Keyword:                  ").append(key).append(OsUtils.LINE_SEPARATOR);
							}

							sb.append("   Help:                   ").append(cliOption.help())
									.append(OsUtils.LINE_SEPARATOR);
							sb.append("   Mandatory:              ").append(cliOption.mandatory())
									.append(OsUtils.LINE_SEPARATOR);
							sb.append("   Default if specified:   '").append(cliOption.specifiedDefaultValue())
									.append("'").append(OsUtils.LINE_SEPARATOR);
							sb.append("   Default if unspecified: '").append(cliOption.unspecifiedDefaultValue())
									.append("'").append(OsUtils.LINE_SEPARATOR);
							sb.append(OsUtils.LINE_SEPARATOR);
						}

					}
					Assert.notNull(cliOption, "CliOption not found for parameter '" + Arrays.toString(annotations)
							+ "'");
				}
			}
			// Only a single argument, so default to the normal help operation
		}

		SortedSet<String> result = new TreeSet<>(COMPARATOR);
		for (MethodTarget mt : matchingTargets) {
			CliCommand cmd = mt.getMethod().getAnnotation(CliCommand.class);
			if (cmd != null) {
				for (String value : cmd.value()) {
					if ("".equals(cmd.help())) {
						result.add("* " + value);
					} else {
						result.add("* " + value + " - " + cmd.help());
					}
				}
			}
		}

		for (String s : result) {
			sb.append(s).append(OsUtils.LINE_SEPARATOR);
		}

		return sb.toString();
	}

	private Collection<MethodTarget> locateTargets(final String buffer, final boolean strictMatching) {
		Assert.notNull(buffer, "Buffer required");
		final Collection<MethodTarget> result = new HashSet<>();

		// The reflection could certainly be optimised, but it's good enough for now (and cached reflection
		// is unlikely to be noticeable to a human being using the CLI)
		for (final CommandMarker command : commands) {
			for (final Method method : command.getClass().getMethods()) {
				CliCommand cmd = method.getAnnotation(CliCommand.class);
				if (cmd != null) {
					// We have a @CliCommand.
					for (String value : cmd.value()) {
						String remainingBuffer = isMatch(buffer, value, strictMatching);
						if (remainingBuffer != null) {
							result.add(new MethodTarget(method, command, remainingBuffer, value));
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * See whether 'buffer' could be an invocation of 'command', and if so, return the remaining part of the buffer.
	 *
	 * @param strictMatching true if ALL words of 'command' need to be matched
	 */
	private static String isMatch(final String buffer, final String command, final boolean strictMatching) {
		Assert.isTrue(command.charAt(command.length() - 1) != ' ', "Command must not end with a space");
		if ("".equals(buffer.trim())) {
			return "";
		}

		if (buffer.length() <= command.length()) {
			// Buffer is shorter or equal in length to command
			int lastSpaceIndex = command.lastIndexOf(' ');
			if (strictMatching && lastSpaceIndex >= 0) {
				// Check buffer touches last command word
				if (buffer.length() < lastSpaceIndex + 2) {
					return null;
				}
			}
			// Just need to check buffer is a prefix of command
			if (command.startsWith(buffer)) {
				return "";
			} else {
				return null;
			}
		} else {
			// Buffer is longer than command. Check command is a prefix of buffer.
			if (!buffer.startsWith(command)) {
				return null;
			}

			String bufferRemaining = buffer.substring(command.length());
			if (bufferRemaining.length() > 0) {
				// Check first char after command is a space
				if (bufferRemaining.charAt(0) != ' ') {
					return null;
				}
				return bufferRemaining.substring(1);
			}
			return bufferRemaining;
		}
	}

}

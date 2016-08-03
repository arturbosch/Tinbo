package com.gitlab.artismarti.tinbo.plugins;

import com.gitlab.artismarti.tinbo.common.Command;
import org.jetbrains.annotations.NotNull;
import org.springframework.shell.core.annotation.CliCommand;

/**
 * @author artur
 */
public class TestCommand implements Command {
	@NotNull
	@Override
	public String getId() {
		return "start";
	}

	@CliCommand("test")
	public String test() {
		return "Test";
	}
}

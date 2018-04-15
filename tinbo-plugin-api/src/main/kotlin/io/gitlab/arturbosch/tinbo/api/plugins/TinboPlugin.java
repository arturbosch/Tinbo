package io.gitlab.arturbosch.tinbo.api.plugins;

import io.gitlab.arturbosch.tinbo.api.marker.Command;

import java.util.List;

/**
 * All Tinbo plugins must implement this interface.
 * This interface pre defines the id used for all plugins.
 * <p>
 * Declaring your implementations of this interface into a
 * META-INF/services/io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
 * file is needed to get your plugins loaded at startup.
 *
 * @author Artur Bosch
 */
public abstract class TinboPlugin {

	String UNSPECIFIED = "unspecified";

	public String name() {
		return getClass().getSimpleName();
	}

	public String version() {
		return UNSPECIFIED;
	}

	public TinboContext context() {
		return ContextAware.INSTANCE.getContext();
	}

	public abstract List<Command> registerCommands(TinboContext tinbo);

	@Override
	public String toString() {
		return name();
	}
}

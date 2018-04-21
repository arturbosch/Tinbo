package io.gitlab.arturbosch.tinbo.api.plugins;

import io.gitlab.arturbosch.tinbo.api.config.TinboMode;
import io.gitlab.arturbosch.tinbo.api.marker.Command;
import org.jetbrains.annotations.Nullable;

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

	static final String UNSPECIFIED = "unspecified";

	public String name() {
		return getClass().getSimpleName();
	}

	public String version() {
		return UNSPECIFIED;
	}

	@Nullable
	public TinboMode providesMode() {
		return null;
	}

	public abstract List<Command> registerCommands(TinboContext tinbo);

	@Override
	public String toString() {
		return name();
	}
}

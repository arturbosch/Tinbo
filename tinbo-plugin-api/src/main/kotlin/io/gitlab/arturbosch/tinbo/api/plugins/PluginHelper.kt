package io.gitlab.arturbosch.tinbo.api.plugins

/**
 * Plugin helpers are objects which exist within Tinbo and provide interfaces to
 * common data which can be evaluated/used by plugins.
 *
 * All plugin helpers get loaded into the {@see SpringContext} class through spring autowiring
 * and can be used through a {@see TinboPlugin}'s context() method.
 *
 *
 *
 * @author Artur Bosch
 */
interface PluginHelper

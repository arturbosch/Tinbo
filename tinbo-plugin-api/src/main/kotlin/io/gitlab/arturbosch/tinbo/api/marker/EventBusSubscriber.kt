package io.gitlab.arturbosch.tinbo.api.marker

/**
 * Marker interface to let the PluginRegistry know, that this bean should be registered
 * by the event bus.
 *
 * To get events, write a method with the @Subscriber annotation and provide the right
 * type of event as first and only parameter.
 *
 * @author Artur Bosch
 */
interface EventBusSubscriber

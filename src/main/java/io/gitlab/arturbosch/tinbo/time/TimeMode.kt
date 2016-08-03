package io.gitlab.arturbosch.tinbo.time

/**
 * Different timer modes: Invalid just tells the timer command that no timing is running.
 * In default mode a timer is visible on the console with white background. The Background mode hides the timer from the user,
 * it still can be stopped with 'stop' or 'q' command or by specifying a stop time.
 *
 * @author artur
 */
enum class TimeMode {
	INVALID, DEFAULT, BACKGROUND
}

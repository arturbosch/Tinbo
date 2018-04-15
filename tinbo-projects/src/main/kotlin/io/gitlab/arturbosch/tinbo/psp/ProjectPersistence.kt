package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.model.Project
import org.yaml.snakeyaml.Yaml

/**
 * @author Artur Bosch
 */

fun Project.toYaml(): String = Yaml(LocalDateRepresenter()).dump(this)

fun String.fromYaml(): Project = Yaml(LocalDateConstructor()).load(this) as Project

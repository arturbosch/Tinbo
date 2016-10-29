package io.gitlab.arturbosch.tinbo.psp;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.time.LocalDate;

public class LocalDateConstructor extends Constructor {

	public LocalDateConstructor() {
		yamlConstructors.put(new Tag("!date"), new ConstructLocalDate());
	}

	protected class ConstructLocalDate extends AbstractConstruct {
		@Override
		public Object construct(Node node) {
			String value = (String) constructScalar((ScalarNode) node);
			long longValue = Long.parseLong(value);
			return longValue == -1 ? null : LocalDate.ofEpochDay(longValue);
		}
	}
}

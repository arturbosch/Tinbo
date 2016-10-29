package io.gitlab.arturbosch.tinbo.psp;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.time.LocalDate;

/**
 * @author Artur Bosch
 */
public class LocalDateRepresenter extends Representer {

	public LocalDateRepresenter() {
		representers.put(LocalDate.class, new RepresentLocalDate());
	}

	protected class RepresentLocalDate implements Represent {
		@Override
		public Node representData(Object data) {
			LocalDate date = (LocalDate) data;
			long value = date == null ? -1 : date.toEpochDay();
			return representScalar(new Tag("!date"), Long.toString(value));
		}
	}
}


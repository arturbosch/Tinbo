package com.gitlab.artismarti.tinbo.printer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Formats a given list with csv strings to a table stored also in a list.
 * This class doesn't check for valid csv strings all with same length, this is up to the user.
 *
 * @author artur
 */
public class CSVTablePrinter {

	public List<String> asTable(List<String> lines) {
		if (lines.isEmpty())
			throw new IllegalArgumentException("No csv data.");

		List<String> table = new ArrayList<>();

		String[] headerEntries = lines.get(0).split(";");

		List<List<String>> columnValues = createListForEachColumn(headerEntries.length);
		fillListsWithValues(columnValues, lines.subList(1, lines.size()));

		int[] columnSizes = withHeaders(headerEntries, calculateSizes(columnValues));

		table.add(formatString(headerEntries, columnSizes));
		table.add(formatHeaderSeparator(columnSizes));

		IntStream.range(0, columnValues.get(0).size())
			.forEach(value -> {
				String[] values = getValuesFromList(value, columnValues);
				table.add(formatString(values, columnSizes));
			});

		return table;
	}

	private String[] getValuesFromList(int value, List<List<String>> columnValues) {
		String[] values = new String[columnValues.size()];
		for (int i = 0; i < columnValues.size(); i++) {
			values[i] = columnValues.get(i).get(value);
		}
		return values;
	}

	private String formatHeaderSeparator(int[] columnSizes) {
		return IntStream.range(0, columnSizes.length)
			.mapToObj(i -> separators(columnSizes[i]))
			.map(value -> value + "+")
			.collect(Collectors.joining());
	}

	private String separators(int size) {
		return fill(size, "-");
	}

	private String fill(int size, String filler) {
		return IntStream.range(0, size)
			.mapToObj(value -> filler)
			.collect(Collectors.joining());
	}

	private String whiteSpaces(int size) {
		return fill(size, " ");
	}

	private String formatString(String[] headerEntries, int[] columnSizes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < headerEntries.length; i++) {
			String headerEntry = headerEntries[i];
			int sizeToFill = columnSizes[i] - headerEntry.length();
			sb.append(headerEntry)
				.append(whiteSpaces(sizeToFill))
				.append("|");
		}
		return sb.toString();
	}

	private int[] withHeaders(String[] headerEntries, int[] ints) {
		int[] sizes = new int[ints.length];
		for (int i = 0; i < ints.length; i++) {
			sizes[i] = Integer.compare(headerEntries[i].length(), ints[i]) == -1
				? ints[i] : headerEntries[i].length();
		}
		return sizes;
	}

	private int[] calculateSizes(List<List<String>> columnValues) {
		return columnValues.stream()
			.mapToInt(this::getMaxSize)
			.toArray();
	}

	private int getMaxSize(List<String> list) {
		return list.stream()
			.max(Comparator.comparingInt(String::length))
			.orElseGet(() -> "")
			.length();
	}

	private void fillListsWithValues(List<List<String>> columnValues, List<String> entries) {
		entries.stream()
			.map(entry -> entry.split(";"))
			.forEach(splits -> {
				for (int i = 0; i < splits.length; i++) {
					columnValues.get(i).add(splits[i]);
				}
			});
	}

	private List<List<String>> createListForEachColumn(int size) {
		return IntStream.range(0, size)
			.mapToObj(entry -> new ArrayList<String>())
			.collect(Collectors.toList());
	}

}

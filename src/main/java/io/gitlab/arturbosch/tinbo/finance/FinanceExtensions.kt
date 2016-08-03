package io.gitlab.arturbosch.tinbo.finance

/**
 * @author artur
 */

fun Sequence<FinanceEntry>.toSummaryStringList(): List<String> {
	return this.toSummaryStringList({ it.category }, {
		it.value.map { it.moneyValue }
				.reduce { money, money2 -> money.plus(money2) }
	})
}

inline fun <K> Sequence<FinanceEntry>.toSummaryStringList(
		keySelector: (FinanceEntry) -> K): List<String> {
	return this.toSummaryStringList(keySelector, {
		it.value.map { it.moneyValue }
				.reduce { money, money2 -> money.plus(money2) }
	})
}

inline fun <K, V> Sequence<FinanceEntry>.toSummaryStringList(
		keySelector: (FinanceEntry) -> K,
		valueTransformation: (Map.Entry<K, List<FinanceEntry>>) -> V): List<String> {
	return this.groupBy { keySelector.invoke(it) }
			.mapValues {
				valueTransformation.invoke(it)
			}
			.map { "${it.key};${it.value.toString()}" }
}

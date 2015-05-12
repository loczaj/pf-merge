package hu.palferi.mergetool.spreadsheet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class ColumnMap<T> extends HashMap<Integer, T> {

	private static final long serialVersionUID = 1L;

	public <T2> ColumnMap<T2> convert(Function<T, T2> operation) {
		ColumnMap<T2> result = new ColumnMap<>();

		this.entrySet().stream().forEach(e -> result.put(e.getKey(), operation.apply(e.getValue())));
		
		return result;
	}

	public void matchTo(Map<Integer, Integer> pairs, ColumnMap<T> column, BiPredicate<T, T> matcher) {

		for (Entry<Integer, T> aEntry : this.entrySet()) {

			for (Entry<Integer, T> bEntry : column.entrySet()) {

				if (pairs.containsKey(aEntry.getKey()))
					break;

				if (pairs.containsValue(bEntry.getKey()))
					continue;

				// Check matching
				if (matcher.test(aEntry.getValue(), bEntry.getValue()))
					pairs.put(aEntry.getKey(), bEntry.getKey());
			}
		}
	}
}

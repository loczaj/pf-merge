package hu.palferi.mergetool.text;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class ListMatcher {

	public static void doMatch(Map<Integer, Integer> pairs, List<String> aList, List<String> bList,
			BiPredicate<String, String> predicate) {

		for (int aIndex = 0; aIndex < aList.size(); aIndex++) {

			for (int bIndex = 0; bIndex < bList.size(); bIndex++) {

				if (pairs.containsKey(aIndex))
					break;

				if (pairs.containsValue(bIndex))
					continue;

				// Check the string value
				if (predicate.test(aList.get(aIndex), bList.get(bIndex)))
					pairs.put(aIndex, bIndex);
			}
		}
	}

	public static void doContainsMatch(Map<Integer, Integer> pairs, List<String> aList,
			List<String> bList) {
		doMatch(pairs, aList, bList, String::contains);
	}

}

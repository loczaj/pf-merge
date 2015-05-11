package hu.palferi.mergetool.text;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListConverter {

	public static List<String> convert(List<String> list, Function<String, String> operation) {
		return list.stream().map(operation).collect(Collectors.toList());
	}

	public static List<String> toLowerCase(List<String> list) {
		return convert(list, String::toLowerCase);
	}

	public static List<String> trim(List<String> list) {
		return convert(list, String::trim);
	}

	public static List<String> removeSpaces(List<String> list) {
		return convert(list, str -> str.replaceAll("\\s+", ""));
	}
}

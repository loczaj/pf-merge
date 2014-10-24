package hu.palferi.mergetool.spreadsheet;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class RowMatcher {

	public static Map<Integer, Integer> doStringContainsMatch(Sheet a, Sheet b, int columnA, int columnB) {

		Map<Integer, Integer> pairs = new HashMap<>();

		for (Row aRow : a) {
			if (pairs.containsKey(aRow.getRowNum()))
				continue;

			for (Row bRow : b) {
				if (pairs.containsValue(bRow.getRowNum()))
					continue;

				Cell aCell = aRow.getCell(columnA);
				Cell bCell = bRow.getCell(columnB);

				// Check type
				if (aCell.getCellType() != Cell.CELL_TYPE_STRING)
					continue;

				if (bCell.getCellType() != Cell.CELL_TYPE_STRING)
					continue;

				// Check the cell data value
				if (contains(aCell.getStringCellValue(), bCell.getStringCellValue()))
					pairs.put(aRow.getRowNum(), bRow.getRowNum());
			}
		}

		return pairs;
	}

	private static boolean contains(String text, String word) {
		return text.trim().toLowerCase().contains(word.trim().toLowerCase());
	}
}

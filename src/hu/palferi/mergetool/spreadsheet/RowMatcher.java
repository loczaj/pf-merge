package hu.palferi.mergetool.spreadsheet;

import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class RowMatcher {

	public static void doStringContainsMatch(Map<Integer, Integer> pairs, Sheet a, Sheet b,
			String columnA, String columnB) {

		for (Row aRow : a) {

			for (Row bRow : b) {

				if (pairs.containsKey(aRow.getRowNum()))
					break;

				if (pairs.containsValue(bRow.getRowNum()))
					continue;

				Cell aCell = aRow.getCell(CellReference.convertColStringToIndex(columnA));
				Cell bCell = bRow.getCell(CellReference.convertColStringToIndex(columnB));

				if (aCell == null || bCell == null)
					continue;

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
	}

	private static boolean contains(String text, String word) {
		return text.trim().toLowerCase().contains(word.trim().toLowerCase());
	}
}

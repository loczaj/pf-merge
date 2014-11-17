package hu.palferi.mergetool.spreadsheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SpreadSheetEditor {

	public static void copyCell(Cell source, Cell destination) {

		// If the old cell is null jump to next cell
		if (source == null) {
			destination = null;
			return;
		}

		// Copy style from old cell and apply to new cell
		CellStyle style = destination.getSheet().getWorkbook().createCellStyle();
		style.cloneStyleFrom(source.getCellStyle());
		destination.setCellStyle(style);

		// If there is a cell comment, copy
		// if (oldCell.getCellComment() != null) {
		// newCell.setCellComment(oldCell.getCellComment());
		// }

		// If there is a cell hyperlink, copy
		// if (oldCell.getHyperlink() != null) {
		// newCell.setHyperlink(oldCell.getHyperlink());
		// }

		// Set the cell data type
		destination.setCellType(source.getCellType());

		// Set the cell data value
		switch (source.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			destination.setCellValue(source.getStringCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			destination.setCellValue(source.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			destination.setCellErrorValue(source.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			destination.setCellFormula(source.getCellFormula());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(source))
				destination.setCellValue(source.getDateCellValue());
			else
				destination.setCellValue(source.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			destination.setCellValue(source.getStringCellValue());
			break;
		}
	}

	public static void copyRow(Row source, Row destination, int[][] drift) {
		for (int[] itinerary : drift) {
			if (source.getCell(itinerary[0]) != null) {
				Cell cell = destination.createCell(itinerary[1]);
				copyCell(source.getCell(itinerary[0]), cell);
			}
		}
	}

	public static void fillColumn(Sheet sheet, int column, String value) {
		for (Row row : sheet) {
			Cell cell = row.createCell(column);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value);
		}
	}
}

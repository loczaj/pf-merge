package hu.palferi.mergetool.spreadsheet;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SpreadSheetEditor {

	public static Cell createCell(Row row, String column, String value) {
		Cell newCell = row.createCell(CellReference.convertColStringToIndex(column));
		newCell.setCellType(Cell.CELL_TYPE_STRING);
		newCell.setCellValue(value);
		return newCell;
	}

	public static Cell createCell(Row row, String column, int value) {
		Cell newCell = row.createCell(CellReference.convertColStringToIndex(column));
		newCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		newCell.setCellValue(value);
		return newCell;
	}

	public static void copyCell(Cell source, Cell destination) {

		// If the old cell is null jump to next cell
		if (source == null) {
			destination = null;
			return;
		}

		// Copy style from old cell and apply to new cell
		// CellStyle style = destination.getSheet().getWorkbook().createCellStyle();
		// style.cloneStyleFrom(source.getCellStyle());
		// destination.setCellStyle(style);

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

	public static void copyRow(Row source, Row destination, String[][] drift) {
		for (String[] itinerary : drift) {
			int sourceColumn = CellReference.convertColStringToIndex(itinerary[0]);
			int destinationColumn = CellReference.convertColStringToIndex(itinerary[1]);
			if (source.getCell(sourceColumn) != null) {
				Cell cell = destination.createCell(destinationColumn);
				copyCell(source.getCell(sourceColumn), cell);
			}
		}
	}

	public static void fillColumn(Sheet sheet, String column, String value) {
		for (Row row : sheet) {
			Cell cell = row.createCell(CellReference.convertColStringToIndex(column));
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value);
		}
	}

	public static void fillColumn(Sheet sheet, String column, int value) {
		for (Row row : sheet) {
			Cell cell = row.createCell(CellReference.convertColStringToIndex(column));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(value);
		}
	}
}

package hu.palferi.mergetool.spreadsheet;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

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

	public static void realignCells(Row source, Row destination, String[][] alignment) {
		for (String[] drift : alignment) {
			int sourceColumn = CellReference.convertColStringToIndex(drift[0]);
			int destinationColumn = CellReference.convertColStringToIndex(drift[1]);
			if (source.getCell(sourceColumn) != null) {
				Cell cell = destination.createCell(destinationColumn);
				copyCell(source.getCell(sourceColumn), cell);
			}
		}
	}

	public static void contractCells(Row source, Row destination, String destinationColumn,
			String[] sourceColumns) {
		String value = new String();
		for (String sourceCulumn : sourceColumns) {
			Cell sourceCell = source.getCell(CellReference.convertColStringToIndex(sourceCulumn));
			if (sourceCell != null) {
				switch (sourceCell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					value += (int) sourceCell.getNumericCellValue();
					value += ".";
					break;
				case Cell.CELL_TYPE_STRING:
					value += sourceCell.getStringCellValue();
					break;
				default:
				}

				value += " ";
			}
		}

		if (!value.isEmpty()) {
			Cell cell = destination.createCell(CellReference.convertColStringToIndex(destinationColumn));
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value.substring(0, value.length() - 1));
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

	public static double getNumericCellValue(Row row, String column) {
		Cell cell = row.getCell(CellReference.convertColStringToIndex(column));

		if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return cell.getNumericCellValue();

		if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
			NumberFormat nf = NumberFormat.getInstance(new Locale("hu", "HU"));
			try {
				return nf.parse(cell.getStringCellValue()).doubleValue();
			} catch (ParseException e) {
			}
		}

		return 0;
	}

	public static String getStringCellValue(Row row, String column) {
		Cell cell = row.getCell(CellReference.convertColStringToIndex(column));

		if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING)
			return cell.getStringCellValue();

		if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return Integer.toString((int) cell.getNumericCellValue());

		return "No string value in cell";
	}

	public static ColumnMap<String> readStringColumn(Sheet sheet, String column) {
		ColumnMap<String> result = new ColumnMap<>();
		Cell cell;

		for (Row row : sheet) {
			cell = row.getCell(CellReference.convertColStringToIndex(column));
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					result.put(row.getRowNum(), cell.getStringCellValue());
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					result.put(row.getRowNum(), Integer.toString((int) cell.getNumericCellValue()));
			}
		}

		return result;
	}

	public static ColumnMap<String> readStringColumns(Sheet sheet, String[] columns, String separator) {
		List<ColumnMap<String>> columnMaps = new ArrayList<>();
		ColumnMap<String> result = new ColumnMap<>();

		for (String column : columns) {
			columnMaps.add(readStringColumn(sheet, column));
		}

		if (columnMaps.get(0).entrySet().size() > 0) {
			for (Entry<Integer, String> entry : columnMaps.get(0).entrySet()) {
				String value = "";
				for (ColumnMap<String> columnMap : columnMaps) {
					value += columnMap.get(entry.getKey()) + separator;
				}

				result.put(entry.getKey(), value.substring(0, value.length() - separator.length()));
			}
		}

		return result;
	}
}

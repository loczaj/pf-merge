package hu.palferi.mergetool;

import hu.palferi.mergetool.spreadsheet.ColumnMap;
import hu.palferi.mergetool.spreadsheet.SpreadSheetEditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomerMaintenance {

	public void run(File transferFile, File registerFile, File kulcsCustomerFile, File newCustomerFile,
			String programName, int stricture) throws InvalidFormatException, IOException {
		Sheet transferSheet = WorkbookFactory.create(transferFile).getSheetAt(0);
		Sheet registrationSheet = WorkbookFactory.create(registerFile).getSheetAt(0);
		Sheet kulcsCustomerSheet = WorkbookFactory.create(kulcsCustomerFile).getSheetAt(0);

		try (FileOutputStream newCustomerStream = new FileOutputStream(newCustomerFile);
				Workbook newCustomerBook = new XSSFWorkbook()) {

			Sheet newCustomerSheet = newCustomerBook.createSheet();

			ColumnMap<String> partners = SpreadSheetEditor.readStringColumn(transferSheet, "I");
			ColumnMap<String> messages = SpreadSheetEditor.readStringColumn(transferSheet, "L");
			ColumnMap<String> participants = SpreadSheetEditor.readStringColumn(registrationSheet, "B");

			Function<String, String> cleanName = str -> str.replaceAll("\\s+", "").toLowerCase();
			Predicate<String> filterName = str -> str.length() > 5;
			partners = partners.filter(filterName).convert(cleanName);
			messages = messages.filter(filterName).convert(cleanName);
			participants = participants.filter(filterName).convert(cleanName);

			Map<Integer, Integer> pairs = new HashMap<>();
			partners.matchTo(participants, String::contains, pairs);
			messages.matchTo(participants, String::contains, pairs);

			Row transferRow, registrationRow, newCustomerRow;
			int rowNumber = 0;
			for (Entry<Integer, Integer> pair : pairs.entrySet()) {

				// System.out.println(set.getKey() + " - " + set.getValue());

				transferRow = transferSheet.getRow(pair.getKey());
				registrationRow = registrationSheet.getRow(pair.getValue());
				newCustomerRow = newCustomerSheet.createRow(rowNumber);

				// Customer book
				//SpreadSheetEditor.createCell(newCustomerRow, "B", customerCode);
				SpreadSheetEditor.realignCells(registrationRow, newCustomerRow, new String[][] {
						{ "B", "A" }, { "D", "O" }, { "K", "C" }, { "L", "D" }, { "M", "E" },
						{ "N", "F" }, { "O", "G" } });

				rowNumber++;
			}

			SpreadSheetEditor.fillColumn(newCustomerSheet, "L", "Magyarország");
			SpreadSheetEditor.fillColumn(newCustomerSheet, "Q", "Átutalás");

			// Write output to files
			newCustomerBook.write(newCustomerStream);
		}
	}
}

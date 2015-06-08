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

			// Read columns needed
			ColumnMap<String> transferName = SpreadSheetEditor.readStringColumn(transferSheet, "I");
			ColumnMap<String> transferMessage = SpreadSheetEditor.readStringColumn(transferSheet, "L");

			ColumnMap<String> regName = SpreadSheetEditor.readStringColumn(registrationSheet, "B");
			ColumnMap<String> regEmail = SpreadSheetEditor.readStringColumn(registrationSheet, "D");
			ColumnMap<String> regAddress = SpreadSheetEditor.readStringColumns(registrationSheet,
					new String[] { "K", "L", "M", "N", "O" }, " ");

			ColumnMap<String> kulcsName = SpreadSheetEditor.readStringColumn(kulcsCustomerSheet, "A");
			ColumnMap<String> kulcsEmail = SpreadSheetEditor.readStringColumn(kulcsCustomerSheet, "G");
			ColumnMap<String> kulcsAddress = SpreadSheetEditor.readStringColumn(kulcsCustomerSheet, "D");

			// Converter, filter
			Function<String, String> dropSpace = str -> str.replaceAll("\\s+", "").toLowerCase();
			Function<String, String> dropSpecial = str -> str.replaceAll("[,./]", "").toLowerCase();
			Predicate<String> lengthFilter = str -> str.length() > 5;

			// Do filtering
			transferName = transferName.filter(lengthFilter).convert(dropSpace);
			transferMessage = transferMessage.filter(lengthFilter).convert(dropSpace);
			regName = regName.filter(lengthFilter).convert(dropSpace);
			regEmail = regEmail.filter(lengthFilter).convert(dropSpace);
			kulcsName = kulcsName.filter(lengthFilter).convert(dropSpace);
			kulcsEmail = kulcsEmail.filter(lengthFilter).convert(dropSpace);

			regAddress = regAddress.filter(lengthFilter).convert(dropSpace).convert(dropSpecial);
			kulcsAddress = kulcsAddress.filter(lengthFilter).convert(dropSpace).convert(dropSpecial);

			// Find matching pairs in transfers - registrations
			Map<Integer, Integer> pairs = new HashMap<>();
			transferName.matchTo(regName, String::contains, pairs);
			transferMessage.matchTo(regName, String::contains, pairs);

			// Find matching pairs in transfers - kulcssoft
			Map<Integer, Integer> matchingNames = new HashMap<>();
			Map<Integer, Integer> matchingEmails = new HashMap<>();
			Map<Integer, Integer> matchingAddresses = new HashMap<>();
			regName.matchTo(kulcsName, String::equals, matchingNames);
			regEmail.matchTo(kulcsEmail, String::equals, matchingEmails);
			regAddress.matchTo(kulcsAddress, String::equals, matchingAddresses);

			// Iterate on matching pairs, create new customer sheet
			Row registrationRow, newCustomerRow, kulcsRow;
			String customerCode;
			int rowNumber = 0;
			for (Entry<Integer, Integer> pair : pairs.entrySet()) {

				customerCode = "";

				// Integer transferRownum = pair.getKey();
				Integer regRownum = pair.getValue();
				Integer kulcsRownum = matchingEmails.get(regRownum);
				
				// System.out.println(regAddress.get(regRownum));

				if (kulcsRownum != null) {
					if (kulcsRownum.equals(matchingNames.get(regRownum))) {
						if (kulcsRownum.equals(matchingAddresses.get(regRownum))) {
							continue;
						} else {
							kulcsRow = kulcsCustomerSheet.getRow(kulcsRownum);
							customerCode = SpreadSheetEditor.getStringCellValue(kulcsRow, "B");
						}
					}
				}

				// transferRow = transferSheet.getRow(transferRownum);
				registrationRow = registrationSheet.getRow(regRownum);
				newCustomerRow = newCustomerSheet.createRow(rowNumber);

				// Customer book
				SpreadSheetEditor.createCell(newCustomerRow, "B", customerCode);
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

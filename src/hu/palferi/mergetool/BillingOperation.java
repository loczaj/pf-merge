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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BillingOperation {

	public void run(File transferFile, File registerFile, File customerOutputFile,
			File billingOutputFile, String customerCodePrefix, int unitPrice, int stricture)
			throws InvalidFormatException, IOException {
		Sheet transferSheet = WorkbookFactory.create(transferFile).getSheetAt(0);
		Sheet registrationSheet = WorkbookFactory.create(registerFile).getSheetAt(0);

		try (FileOutputStream customerOutputStream = new FileOutputStream(customerOutputFile);
				FileOutputStream billingOutputStream = new FileOutputStream(billingOutputFile);
				Workbook customerBook = new XSSFWorkbook();
				Workbook billingBook = new XSSFWorkbook()) {

			Sheet customerSheet = customerBook.createSheet();
			Sheet billingSheet = billingBook.createSheet();

			ColumnMap<String> partners = SpreadSheetEditor.readStringColumn(transferSheet, "I");
			ColumnMap<String> messages = SpreadSheetEditor.readStringColumn(transferSheet, "L");
			ColumnMap<String> participants = SpreadSheetEditor.readStringColumn(registrationSheet, "B");

			Function<String, String> cleanString = str -> str.replaceAll("\\s+", "").toLowerCase();
			partners = partners.convert(cleanString);
			messages = messages.convert(cleanString);
			participants = participants.convert(cleanString);

			Map<Integer, Integer> pairs = new HashMap<>();
			partners.matchTo(pairs, participants, String::contains);
			messages.matchTo(pairs, participants, String::contains);

			// RowMatcher.doStringContainsMatch(pairs, transferSheet, registrationSheet, "I", "B");
			// RowMatcher.doStringContainsMatch(pairs, transferSheet, registrationSheet, "L", "B");

			Row transferRow, registrationRow, customerRow, billingRow;
			int rowNumber = 0;
			for (Entry<Integer, Integer> pair : pairs.entrySet()) {

				// System.out.println(set.getKey() + " - " + set.getValue());

				transferRow = transferSheet.getRow(pair.getKey());
				registrationRow = registrationSheet.getRow(pair.getValue());
				customerRow = customerSheet.createRow(rowNumber);
				billingRow = billingSheet.createRow(rowNumber);

				// Customer book
				String customerCode = String.format("%s%03d", customerCodePrefix, rowNumber + 1);
				SpreadSheetEditor.createCell(customerRow, "B", customerCode);
				SpreadSheetEditor.realignCells(registrationRow, customerRow, new String[][] {
						{ "B", "A" }, { "D", "O" }, { "K", "C" }, { "L", "D" }, { "M", "E" },
						{ "N", "F" }, { "O", "G" } });

				// Billing book
				SpreadSheetEditor.createCell(billingRow, "A", rowNumber + 1);
				SpreadSheetEditor.createCell(billingRow, "D", customerCode);
				SpreadSheetEditor.contractCells(customerRow, billingRow, "H", new String[] { "E", "F",
						"G" });
				SpreadSheetEditor.realignCells(customerRow, billingRow, new String[][] { { "A", "E" },
						{ "C", "F" }, { "D", "G" } });
				SpreadSheetEditor.realignCells(transferRow, billingRow, new String[][] { { "C", "L" },
						{ "C", "M" } });

				int amount = (int) SpreadSheetEditor.getNumericCellValue(transferRow, "J");
				SpreadSheetEditor.createCell(billingRow, "O", amount / unitPrice);
				SpreadSheetEditor.createCell(billingRow, "Q", String.format("%.2f", unitPrice / 1.27D));

				if (amount % unitPrice != 0) {
					SpreadSheetEditor.createCell(billingRow, "V",
							String.format("Túlfizetés: %d Forint", amount % unitPrice));
				}

				rowNumber++;
			}

			SpreadSheetEditor.fillColumn(customerSheet, "L", "Magyarország");
			SpreadSheetEditor.fillColumn(customerSheet, "Q", "Átutalás");

			SpreadSheetEditor.fillColumn(billingSheet, "B", "HUF");
			SpreadSheetEditor.fillColumn(billingSheet, "C", 1);
			SpreadSheetEditor.fillColumn(billingSheet, "J", "Átutalás");
			SpreadSheetEditor.fillColumn(billingSheet, "P", "db");
			SpreadSheetEditor.fillColumn(billingSheet, "R",
					"A számla közvetített szolgáltatást tartalmaz.");
			SpreadSheetEditor.fillColumn(billingSheet, "S", "27%");

			// Write output to files
			customerBook.write(customerOutputStream);
			billingBook.write(billingOutputStream);
		}
	}
}

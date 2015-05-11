package hu.palferi.mergetool;

import hu.palferi.mergetool.spreadsheet.RowMatcher;
import hu.palferi.mergetool.spreadsheet.SpreadSheetEditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
		Sheet transfers = WorkbookFactory.create(transferFile).getSheetAt(0);
		Sheet registrations = WorkbookFactory.create(registerFile).getSheetAt(0);

		try (FileOutputStream customerOutputStream = new FileOutputStream(customerOutputFile);
				FileOutputStream billingOutputStream = new FileOutputStream(billingOutputFile);
				Workbook customerBook = new XSSFWorkbook();
				Workbook billingBook = new XSSFWorkbook()) {

			Sheet customerSheet = customerBook.createSheet();
			Sheet billingSheet = billingBook.createSheet();

			Map<Integer, Integer> pairs = new HashMap<>();
			RowMatcher.doStringContainsMatch(pairs, transfers, registrations, "I", "B");
			RowMatcher.doStringContainsMatch(pairs, transfers, registrations, "L", "B");

			Row transferRow, registrationRow, customerRow, billingRow;
			int rowNumber = 0;
			for (Entry<Integer, Integer> pair : pairs.entrySet()) {

				// System.out.println(set.getKey() + " - " + set.getValue());

				transferRow = transfers.getRow(pair.getKey());
				registrationRow = registrations.getRow(pair.getValue());
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

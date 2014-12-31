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
			// Sheet billingSheet = billingBook.createSheet();

			Map<Integer, Integer> pairs = new HashMap<>();
			RowMatcher.doStringContainsMatch(pairs, transfers, registrations, "I", "B");
			RowMatcher.doStringContainsMatch(pairs, transfers, registrations, "L", "B");

			Row newRow;
			int rowNumber = 0;
			for (Entry<Integer, Integer> pair : pairs.entrySet()) {

				// System.out.println(set.getKey() + " - " + set.getValue());

				newRow = customerSheet.createRow(rowNumber++);
				String customerCode = String.format("%s%03d", customerCodePrefix, rowNumber);
				SpreadSheetEditor.createStringCell(newRow, "B", customerCode);
				SpreadSheetEditor.copyRow(registrations.getRow(pair.getValue()), newRow, new String[][] {
						{ "B", "A" }, { "D", "O" }, { "K", "C" }, { "L", "D" }, { "M", "E" },
						{ "N", "F" }, { "O", "G" } });
			}

			SpreadSheetEditor.fillColumn(customerSheet, "L", "Magyarország");
			SpreadSheetEditor.fillColumn(customerSheet, "Q", "Átutalás");

			// Write output to files
			customerBook.write(customerOutputStream);
			billingBook.write(billingOutputStream);
		}
	}
}

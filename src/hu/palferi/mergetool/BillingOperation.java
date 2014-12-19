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

	private File transferFile;
	private File registerFile;

	// private File customerFile;

	public BillingOperation(File transferFile, File registerFile) {
		this.transferFile = transferFile;
		this.registerFile = registerFile;
	}

	public void run(File outputFile, int stricture) throws InvalidFormatException, IOException {
		Sheet transfers = WorkbookFactory.create(transferFile).getSheetAt(0);
		Sheet registrations = WorkbookFactory.create(registerFile).getSheetAt(0);

		Workbook newCustomers = new XSSFWorkbook();
		Sheet newCustomersSheet = newCustomers.createSheet();

		Map<Integer, Integer> pairs = new HashMap<>();
		RowMatcher.doStringContainsMatch(pairs, transfers, registrations, "I", "B");
		RowMatcher.doStringContainsMatch(pairs, transfers, registrations, "L", "B");

		Row newRow;
		int rowNumber = 0;
		for (Entry<Integer, Integer> pair : pairs.entrySet()) {

			// System.out.println(set.getKey() + " - " + set.getValue());

			newRow = newCustomersSheet.createRow(rowNumber++);
			SpreadSheetEditor.copyRow(registrations.getRow(pair.getValue()), newRow, new String[][] {
					{ "B", "A" }, { "D", "O" }, { "K", "C" }, { "L", "D" }, { "M", "E" }, { "N", "F" },
					{ "O", "G" } });
		}

		SpreadSheetEditor.fillColumn(newCustomersSheet, "L", "Magyarország");
		SpreadSheetEditor.fillColumn(newCustomersSheet, "Q", "Átutalás");

		// Write the output to a file
		FileOutputStream outpuStream = new FileOutputStream(outputFile);
		newCustomers.write(outpuStream);
		outpuStream.close();
	}
}

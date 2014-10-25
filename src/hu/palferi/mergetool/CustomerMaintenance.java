package hu.palferi.mergetool;

import hu.palferi.mergetool.spreadsheet.RowMatcher;
import hu.palferi.mergetool.spreadsheet.SpreadSheetEditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomerMaintenance {

	private File transferFile;
	private File registerFile;

	// private File customerFile;

	public CustomerMaintenance(File transferFile, File registerFile, File customerFile) {
		this.transferFile = transferFile;
		this.registerFile = registerFile;
		// this.customerFile = customerFile;
	}

	public void run(File outputFile, int stricture) throws InvalidFormatException, IOException {
		Sheet transfers = WorkbookFactory.create(transferFile).getSheetAt(0);
		Sheet registrations = WorkbookFactory.create(registerFile).getSheetAt(0);
		// Sheet customers = WorkbookFactory.create(customerFile).getSheetAt(0);

		Workbook newCustomers = new XSSFWorkbook();
		Sheet newCustomersSheet = newCustomers.createSheet();

		Map<Integer, Integer> pairs = RowMatcher.doStringContainsMatch(transfers, registrations, 8, 1);
		pairs.putAll(RowMatcher.doStringContainsMatch(transfers, registrations, 11, 1));

		Row newRow;
		int rowNumber = 0;
		for (Entry<Integer, Integer> pair : pairs.entrySet()) {
			// System.out.println(set.getKey() + " - " + set.getValue());
			newRow = newCustomersSheet.createRow(rowNumber++);

			SpreadSheetEditor.copyRow(registrations.getRow(pair.getKey()), newRow, new int[][] {
					{ 1, 0 }, { 3, 14 }, { 10, 2 }, { 11, 3 }, { 12, 4 }, { 13, 5 }, { 14, 6 } });
		}

		SpreadSheetEditor.fillColumn(newCustomersSheet, 11, "Magyarország");
		SpreadSheetEditor.fillColumn(newCustomersSheet, 16, "Átutalás");

		// Write the output to a file
		FileOutputStream outpuStream = new FileOutputStream(outputFile);
		newCustomers.write(outpuStream);
		outpuStream.close();
	}
}

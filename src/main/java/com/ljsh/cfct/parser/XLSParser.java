package com.ljsh.cfct.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.RowData;
import com.ljsh.cfct.core.TableData;

public class XLSParser {

	private final File file;

	public XLSParser(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public List<TableData> parse() {
		InputStream is = null;
		List<TableData> tables = new ArrayList<TableData>();

		try {
			is = new FileInputStream(file);
			Workbook wb = null;
			try {
				wb = new HSSFWorkbook(is);
			} catch (IOException e) {
				wb = new XSSFWorkbook(is);
			}
			int sheetCount = wb.getNumberOfSheets();
			for (int index = 0; index < sheetCount; index++) {
				Sheet sheet = wb.getSheetAt(index);
				TableData data = parseSheet(sheet);
				if (data != null) {
					tables.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return tables;
	}

	public TableData parseSheet(Sheet sheet) {

		// Decide which rows to process
		int rowStart = sheet.getFirstRowNum();
		int rowEnd = sheet.getLastRowNum();
		int headerRow = 3;
		if (rowEnd < rowStart + headerRow) {
			return null;
		}
		TableData tableData = new TableData();
		tableData.setTableName(sheet.getSheetName());
		parseTableDataField(sheet, rowStart, tableData);

		for (int rowNum = rowStart + headerRow; rowNum < rowEnd; rowNum++) {

			Row row = sheet.getRow(rowNum);
			if (row == null) {
				// This whole row is empty
				// Handle it as needed
				continue;
			}

			RowData rowData = new RowData(rowNum);
			for (FieldInfo info : tableData.getIndexToFieldInfos().values()) {
				Cell c1 = row.getCell(info.getColumnIndex(), Row.RETURN_BLANK_AS_NULL);
				if (c1 != null) {
					rowData.addData(info.getColumnIndex(), c1.getStringCellValue());
				} else {
					rowData.addData(info.getColumnIndex(), StringUtils.EMPTY);
				}
			}
			tableData.addRowData(rowData);
		}

		return tableData;
	}

	private void parseTableDataField(Sheet sheet, int rowStart, TableData tableData) {
		Row fieldRow = sheet.getRow(rowStart);
		int lastColumn = fieldRow.getLastCellNum();
		for (int cn = 0; cn < lastColumn; cn++) {
			Cell c = fieldRow.getCell(cn, Row.RETURN_BLANK_AS_NULL);
			if (c != null) {
				// Do something useful with the cell's contents
				FieldInfo info = new FieldInfo();
				info.setColumnIndex(cn);
				info.setColumnName(c.getStringCellValue());

				Row sideRow = sheet.getRow(rowStart + 1);
				Cell c1 = sideRow.getCell(cn, Row.RETURN_BLANK_AS_NULL);
				if (c1 != null) {
					String sideCountent = c.getStringCellValue();
					if (sideCountent.contains("C") || sideCountent.contains("c")) {
						info.setClient(true);
					}
					if (sideCountent.contains("S") || sideCountent.contains("c")) {
						info.setServer(true);
					}
				}
				Row typeRow = sheet.getRow(rowStart + 2);
				Cell c2 = typeRow.getCell(cn, Row.RETURN_BLANK_AS_NULL);
				if (c2 != null) {
					info.setDataType(c.getStringCellValue());
				} else {
					info.setDataType("string");
				}
				tableData.addFieldInfo(info);
			}
		}
	}

}

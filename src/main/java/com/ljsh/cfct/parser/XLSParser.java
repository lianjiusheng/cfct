package com.ljsh.cfct.parser;

import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.RowData;
import com.ljsh.cfct.core.TableData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class XLSParser {

	private final File file;

	public XLSParser(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}
    public static SimpleDateFormat default_date_format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public List<TableData> parse() {
		InputStream is = null;
		List<TableData> tables = new ArrayList<TableData>();

		try {
			is = new FileInputStream(file);
			Workbook wb = null;
			if(file.getCanonicalPath().endsWith(".xls")){
				wb = new HSSFWorkbook(is);
			}else {
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
					rowData.addData(info.getColumnIndex(), getCellValue(c1));
				} else {
					rowData.addData(info.getColumnIndex(), StringUtils.EMPTY);
				}
			}
			tableData.addRowData(rowData);
		}

		return tableData;
	}

	private String getCellValue(Cell c1 ){

		Object value=null;
		switch (c1.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = c1.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = c1.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(c1)) {
                    value = c1.getDateCellValue();
                    value = default_date_format.format(value);
                } else {
                    value = c1.getNumericCellValue();
                }
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = c1.getCellFormula();
                break;
            default:
                value = StringUtils.EMPTY;
                break;
        }
		return String.valueOf(value);
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

package com.ljsh.cfct.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TableData {

	private String tableName;

	private final Map<Integer, FieldInfo> indexToFieldInfos;

	private final Map<Integer, RowData> rows;

	public TableData() {

		indexToFieldInfos = new HashMap<Integer, FieldInfo>();
		rows = new TreeMap<Integer, RowData>();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<Integer, FieldInfo> getIndexToFieldInfos() {
		return indexToFieldInfos;
	}

	public void addFieldInfo(FieldInfo info) {
		indexToFieldInfos.put(info.getColumnIndex(), info);
	}

	public Map<Integer, RowData> getRows() {
		return rows;
	}

	public void addRowData(RowData data) {
		rows.put(data.getRow(), data);
	}

	public Map<String, Object> getFieldDataMap(int row) {
		if (rows.isEmpty() || !rows.containsKey(row)) {
			return new TreeMap<String, Object>();
		}
		return getFieldDataMap(rows.get(row));
	}

	public Map<String, Object> getFieldDataMap(RowData data) {
		Map<String, Object> map = new TreeMap<String, Object>();
		for (Entry<Integer, FieldInfo> e : indexToFieldInfos.entrySet()) {
			map.put(e.getValue().getColumnName(), getData(data, e.getValue()));
		}
		return map;
	}

	private Object getData(RowData row, FieldInfo fieldInfo) {

		String dataType = fieldInfo.getDataType();
		int index = fieldInfo.getColumnIndex();

		if ("string".equalsIgnoreCase(dataType)) {
			return row.getString(index);
		}
		if ("int".equalsIgnoreCase(dataType)) {
			return row.getInt(index);
		}
		if ("long".equalsIgnoreCase(dataType)) {
			return row.getLong(index);
		}
		if ("float".equalsIgnoreCase(dataType)) {
			return row.getFloat(index);
		}
		if ("double".equalsIgnoreCase(dataType)) {
			return row.getDouble(index);
		}
		if ("date".equalsIgnoreCase(dataType)) {
			return row.getData(index);
		}
		if ("number".equalsIgnoreCase(dataType)) {
			return row.getInt(index);
		}
		throw new RuntimeException("Unsupported data type:" + fieldInfo.getDataType() + ",fieldName:" + fieldInfo.getColumnName());
	}


	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("table:").append(getTableName()).append("\n");
		sb.append(rows.values());
		return sb.toString();
	}
}

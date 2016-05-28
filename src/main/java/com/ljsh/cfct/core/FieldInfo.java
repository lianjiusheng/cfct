package com.ljsh.cfct.core;

public class FieldInfo {

	public static int SIDE_CLIENT=1;
	public static int SIDE_SERVER=2;
	private int columnIndex;
	private String columnName;
	private String dataType;
	private int side;

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

}

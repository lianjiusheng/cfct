package com.ljsh.cfct.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class RowData {

	private final int row;

	private final Map<Integer, String> datas;

	public RowData(int row) {
		this.row = (row);
		this.datas = new TreeMap<Integer, String>();
	}

	public int getRow() {
		return row;
	}

	public void addData(int index, String data) {
		datas.put(index, data);
	}

	public String getData(int index) {
		return datas.get(index);
	}

	public String getString(int index) {
		return getData(index);
	}

	public int getInt(int index) {
		return (int) getDouble(index);
	}

	public float getFloat(int index) {
        return (float) getDouble(index);
	}

	public double getDouble(int index) {
		String data = getData(index);
		return Double.valueOf(StringUtils.isBlank(data) ? "0" : data);
	}

	public long getLong(int index) {
        return (long) getDouble(index);
	}

	public Date getDate(int index, String pattern) {
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		return getDate(index, fmt);
	}

	public Date getDate(int index, DateFormat fmt) {
		String data = getData(index);
		if (StringUtils.isBlank(data)) {
			return null;
		}
		try {
			return fmt.parse(getData(index));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("row-").append(row).append("=").append(datas);
		return sb.toString();
	}
}

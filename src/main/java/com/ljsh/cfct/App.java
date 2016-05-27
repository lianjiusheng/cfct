package com.ljsh.cfct;

import java.io.File;
import java.util.List;

import com.ljsh.cfct.core.TableData;
import com.ljsh.cfct.parser.XLSParser;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {

		XLSParser parser = new XLSParser(new File("C:\\Users\\Administrator\\Desktop\\demo.xlsx"));
		List<TableData> list = parser.parse();
		System.out.println(list);
	}
}

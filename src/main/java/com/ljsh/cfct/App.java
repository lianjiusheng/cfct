package com.ljsh.cfct;

import java.io.File;
import java.util.List;

import com.ljsh.cfct.converter.Converter;
import com.ljsh.cfct.converter.JSONConverter;
import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.TableData;
import com.ljsh.cfct.parser.XLSParser;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {


		XLSParser parser = new XLSParser(new File("E:\\GithubWorkspace\\cfct\\src\\main\\resources\\demo.xlsx"));
		List<TableData> list = parser.parse();
		//System.out.println(list);
		File directory=new File("E:\\GithubWorkspace\\cfct\\src\\main\\resources\\");
		for (TableData table:list){
			Converter c=new JSONConverter(directory,table);
			try {
				c.exeConvert(FieldInfo.SIDE_CLIENT|FieldInfo.SIDE_SERVER);
			}catch (Throwable e){
				e.printStackTrace();
			}
		}
	}
}

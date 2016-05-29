package com.ljsh.cfct.converter;

import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.RowData;
import com.ljsh.cfct.core.TableData;
import com.ljsh.cfct.parser.XLSParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by hyy on 2016/5/28.
 */
public class SQLConverter extends Converter {


    public SQLConverter(File directory, TableData tableData) {
       super(directory,tableData);
    }


    protected void convert(int side, File directory) throws Throwable{

        TableData tableData=getTableData();
        String tableName=tableData.getTableName();

        checkOutDirectory();

        if(directory==null){
            return ;
        }

        File f=new File(directory,tableData.getTableName()+".sql");
        FileWriter writer=null;
        try {
             writer = new FileWriter(f);

            for (Map.Entry<Integer, RowData> entry : tableData.getRows().entrySet()) {
                Map<String, Object> data = tableData.getFieldDataMap(entry.getValue(), side);
                Set<String> keySet = data.keySet();
                StringBuilder stringBuilder = new StringBuilder("insert into ").append(tableName).append("(");
                for (String key : keySet) {
                    stringBuilder.append(key).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                stringBuilder.append(") values (");

                for (String key : keySet) {
                    Object val = data.get(key);
                    if (val != null && val instanceof Date) {
                        val = XLSParser.default_date_format.format(val);
                    }
                    if (val instanceof String){
                        stringBuilder.append("\"").append(val).append("\"");
                    }else {
                        stringBuilder.append(val);
                    }
                    stringBuilder.append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                stringBuilder.append(");");
                writer.write(stringBuilder.toString());
                writer.write("\n");
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(writer!=null){
                try {
                    writer.flush();
                    writer.close();
                }catch (IOException e){
                }
            }
        }
    }
}

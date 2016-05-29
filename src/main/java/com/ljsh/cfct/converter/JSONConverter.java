package com.ljsh.cfct.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.RowData;
import com.ljsh.cfct.core.TableData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hyy on 2016/5/28.
 */
public class JSONConverter extends Converter {

   private ObjectMapper objectMapper=new ObjectMapper();

    public JSONConverter(File directory, TableData tableData) {
      super(directory,tableData);
    }

    protected void convert(int side, File directory) throws Throwable {

        TableData tableData=getTableData();
        String tableName=tableData.getTableName();

        if(directory==null){
            return ;
        }

        List<Map<String,Object>> datas=new ArrayList<Map<String, Object>>(tableData.getRows().size());
        for(Map.Entry<Integer,RowData> entry: tableData.getRows().entrySet()){
            datas.add( tableData.getFieldDataMap(entry.getValue(), side));
        }
        String content= objectMapper.writeValueAsString(datas);

        File f=new File(directory,tableData.getTableName()+".json");
        FileOutputStream os=null;
        try {
            os=new FileOutputStream(f);
            os.write(content.getBytes());
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if (os!=null){
                try{
                os.flush();
                os.close();
                }catch (IOException e){
                }
            }
        }
    }



}

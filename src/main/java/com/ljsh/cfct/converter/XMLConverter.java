package com.ljsh.cfct.converter;

import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.RowData;
import com.ljsh.cfct.core.TableData;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by hyy on 2016/5/28.
 */
public class XMLConverter extends Converter {

    public XMLConverter(File directory, TableData tableData) {
        super(directory,tableData);
    }


    protected void convert(int side, File directory) throws Throwable{

        TableData tableData=getTableData();
        String tableName=tableData.getTableName();

        checkOutDirectory();

        if(directory==null){
            return ;
        }

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("list" );

        for(Map.Entry<Integer,RowData> entry: tableData.getRows().entrySet()){
          Map<String,Object> data= tableData.getFieldDataMap(entry.getValue(), side);
            Element dataElement=root.addElement(tableName);
            for(Map.Entry<String,Object> entry1: data.entrySet()){
                Element cElement= dataElement.addElement(entry1.getKey());
                cElement.setText(String.valueOf(entry1.getValue()));
            }
        }

        File f=new File(directory,tableData.getTableName()+".xml");
        // lets write to a file
        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileWriter(f) );
            writer.write(document);
        } catch (IOException e) {
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

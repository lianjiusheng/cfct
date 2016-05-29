package com.ljsh.cfct.converter;

import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.TableData;

import java.io.File;

/**
 * Created by hyy on 2016/5/28.
 */
public abstract class Converter {

    private final File directory;
    private final TableData tableData;
    private File clientDirectory;
    private File serverDirectory;

    public File getClientDirectory() {
        return clientDirectory;
    }

    public File getServerDirectory() {
        return serverDirectory;
    }

    public Converter(File directory, TableData tableData) {
        this.directory = directory;
        this.tableData = tableData;
    }

    public File getDirectory() {
        return directory;
    }

    public TableData getTableData() {
        return tableData;
    }

    protected void checkOutDirectory(){
        checkDirectory(directory);
        clientDirectory=new File(directory,"client");
        checkDirectory(clientDirectory);
        serverDirectory=new File(directory,"server");
        checkDirectory(serverDirectory);
    }

    private void checkDirectory(File directory){
        if(!directory.exists()){
            directory.mkdirs();
        }
    }


    public  void exeConvert(int side) throws Throwable{

        checkOutDirectory();

        if ((side& FieldInfo.SIDE_SERVER)!=0){
            convert(FieldInfo.SIDE_SERVER, getServerDirectory());
        }

        if ((side& FieldInfo.SIDE_CLIENT)!=0){
            convert(FieldInfo.SIDE_CLIENT, getClientDirectory());
        }
    }


    protected abstract void convert(int side, File directory) throws Throwable;
}

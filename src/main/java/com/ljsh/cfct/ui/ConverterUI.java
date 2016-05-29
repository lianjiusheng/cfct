package com.ljsh.cfct.ui;

import com.ljsh.cfct.converter.Converter;
import com.ljsh.cfct.converter.JSONConverter;
import com.ljsh.cfct.converter.SQLConverter;
import com.ljsh.cfct.converter.XMLConverter;
import com.ljsh.cfct.core.FieldInfo;
import com.ljsh.cfct.core.TableData;
import com.ljsh.cfct.parser.XLSParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import java.util.List;

/**
 * Created by hyy on 2016/5/28.
 */
public class ConverterUI{

    private JPanel mainPanel;
    private JTextField selectTextField;
    private JButton selectButton;
    private JRadioButton jsonRadioButton;
    private JRadioButton xmlRadioButton;
    private JRadioButton sqlRadioButton;
    private JCheckBox clientCheckBox;
    private JCheckBox serverCheckBox;
    private JButton convertButton;
    private JTextField outTextField;
    private JButton selectOutButton;
    private ButtonGroup formatGroup;

    private File outDirectory;
    private File selectFile;
    private String format;
    private int use;

    public ConverterUI() {

        selectOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                jfc.showDialog(mainPanel, "选择");
                outDirectory = jfc.getSelectedFile();
                outTextField.setText(outDirectory.getAbsolutePath());
            }
        });

        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                jfc.showDialog(mainPanel, "选择");
                selectFile = jfc.getSelectedFile();
                selectTextField.setText(selectFile.getAbsolutePath());
            }
        });


        FormateActionListener formateActionListener=new FormateActionListener();
        jsonRadioButton.addActionListener(formateActionListener);
        xmlRadioButton.addActionListener(formateActionListener);
        sqlRadioButton.addActionListener(formateActionListener);

        UseItemChanageListener listener=new UseItemChanageListener();
        clientCheckBox.addItemListener(listener);
        serverCheckBox.addItemListener(listener);

        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("format:"+ format);

                File source=selectFile;
                java.util.List<File> fileList=new ArrayList<File>();
                if(source.isDirectory()){
                    for(File f:source.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".xls")||name.endsWith(".xlsx");
                        }
                    })){
                        fileList.add(f);
                    }
                }else{
                    fileList.add(source);
                }

                for (File file:fileList) {
                    XLSParser parser=new XLSParser(file);
                    List<TableData> tableDatas=parser.parse();
                    for(TableData data:tableDatas){
                        Converter converter=createConverter(format,outDirectory,data);
                        if (converter==null){
                            System.err.println("not support format:"+format);
                            continue;
                        }
                        try {
                            converter.exeConvert(use);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private Converter createConverter(String format,File directory, TableData tableData){

        if("json".equals(format)){
            return new JSONConverter(directory,tableData);
        }
        if ("xml".equals(format)){
            return new XMLConverter(directory,tableData);
        }

        if ("sql".equals(format)){
            return new SQLConverter(directory,tableData);
        }
        return null;
    }

    private class FormateActionListener implements  ActionListener{

        public void actionPerformed(ActionEvent e) {
           Object object= e.getSource();

            if (object==jsonRadioButton){
                format ="json";
            }else if (object==xmlRadioButton){
                format ="xml";
            }else if(object==sqlRadioButton){
                format ="sql";
            }
        }
    }

    private class UseItemChanageListener implements ItemListener{


        public void itemStateChanged(ItemEvent e) {
            Object source = e.getItemSelectable();
            if (source == clientCheckBox) {
              if (clientCheckBox.isSelected()){
                  use|= FieldInfo.SIDE_CLIENT;
              }else{
                  use&=~FieldInfo.SIDE_CLIENT;
              }
            } else if (source == serverCheckBox) {
                if (serverCheckBox.isSelected()){
                    use|= FieldInfo.SIDE_SERVER;
                }else{
                    use&=~FieldInfo.SIDE_SERVER;
                }
            }

        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Excel格式转换");
        frame.setContentPane(new ConverterUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width=480;
        int height=320;
        //获取屏幕大小
        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((screenSize.width-width)/2,(screenSize.height-height)/2,width,height);
        frame.setResizable(false);
        //frame.pack();
        frame.setVisible(true);
    }
}

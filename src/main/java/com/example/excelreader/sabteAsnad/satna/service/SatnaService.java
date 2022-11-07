package com.example.excelreader.sabteAsnad.satna.service;

import com.example.excelreader.sabteAsnad.Helper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

@Service
public class SatnaService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public SatnaService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("ساتنا");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;
    }

    public void createSatnaExcel() throws SQLException, IOException {

        String satnaVarede = "(select amount,trn,value_date from asnad.aria) as aria inner join " +
                "(select amount,trn from asnad.c2cvarede_entity) as varede on aria.trn = varede.trn " +
                "and aria.amount = varede.amount " +
                "where original_message_type = 'MQ103'" +
                "and credit_party = 'NOORIRTHXXX' and message_status = 'Settled'";

        String satnaSadere = "select amount,trn,value_date from asnad.aria where original_message_type = 'MQ103'" +
                "and credit_party = 'NOORIRTHXXX' and message_status = 'Settled'";

        PreparedStatement varedeStatement = Helper.getConnection().prepareStatement(satnaVarede);
        PreparedStatement sadereStatement = Helper.getConnection().prepareStatement(satnaSadere);
        ResultSet varedeResultSet = varedeStatement.executeQuery();
        ResultSet sadereResultSet = sadereStatement.executeQuery();

        satnaVarede(varedeResultSet);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Satna.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void satnaVarede(ResultSet varede) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند حسابداری تسویه رقم تراکنش های وارده ساتنا");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
    }





}

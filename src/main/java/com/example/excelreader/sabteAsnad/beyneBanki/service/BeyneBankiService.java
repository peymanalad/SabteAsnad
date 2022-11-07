package com.example.excelreader.sabteAsnad.beyneBanki.service;

import com.example.excelreader.sabteAsnad.Helper;
import com.ibm.icu.impl.duration.impl.Utils;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;
import com.ibm.icu.util.ULocale;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;


@Service
public class BeyneBankiService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public BeyneBankiService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("بین بانکی");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;

    }

    public void createBeyneBankiExcel() throws SQLException, IOException {
        String varede = "select * from asnad.aria as aria inner join " +
                "(select transaction_id,amount from asnad.b2bvarede_entity) as varede on " +
                "aria.trn = varede.transaction_id " +
                "where original_message_type = 'MQ202' and message_status = 'Settled' " +
                "and aria.credit_party = 'NOORIRTHXXX' and aria.value_date = ?";

        String sadere = "select * from asnad.aria as aria inner join " +
                "(select transaction_id,amount from asnad.b2bsadere_entity) as sadere on " +
                "aria.trn = sadere.transaction_id " +
                "where original_message_type = 'MQ202' and message_status = 'Settled' " +
                "and aria.debit_party = 'NOORIRTHXXX' and aria.value_date = ?";

        PreparedStatement sadereStatement = Helper.getConnection().prepareStatement(sadere);
        PreparedStatement varedeStatement = Helper.getConnection().prepareStatement(varede);
        sadereStatement.setString(1,Helper.getYesterdayDate());
        varedeStatement.setString(1,Helper.getYesterdayDate());
        ResultSet sadereResultSet = sadereStatement.executeQuery();
        ResultSet varedeResultSet = varedeStatement.executeQuery();

        receiveFacilities(varedeResultSet);
        payOffFacilities(sadereResultSet);
        payOffFacilitiesInterest(sadereResultSet);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Excel.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void receiveFacilities(ResultSet varede) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند دریافت تسهیلات در سامانه نامی");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        while (varede.next()) {
            Row row = sheet.createRow(this.rowCount++);
            row.createCell(column).setCellValue(j++);
            row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 1).setCellValue("");
            row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 2).setCellValue("");
            row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 3).setCellValue("");
            row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 4).setCellValue("");
            row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 5).setCellValue(varede.getLong(2));
            row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 6).setCellValue("");
            row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
            Row row2 = sheet.createRow(this.rowCount++);
            row2.createCell(column).setCellValue(j++);
            row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 1).setCellValue("");
            row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 2).setCellValue("");
            row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 3).setCellValue("");
            row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 4).setCellValue("");
            row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 5).setCellValue("");
            row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 6).setCellValue(varede.getLong(14));
            row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        }
    }

    public void payOffFacilities(ResultSet sadere) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسهیلات دریافتی در سامانه نامی");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        while (sadere.next()) {
            Row row = sheet.createRow(this.rowCount++);
            row.createCell(column).setCellValue(j++);
            row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 1).setCellValue("0650");
            row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 2).setCellValue("");
            row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 3).setCellValue("");
            row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 4).setCellValue("");
            row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 5).setCellValue(sadere.getLong(2));
            row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 6).setCellValue("");
            row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
            Row row2 = sheet.createRow(this.rowCount++);
            row2.createCell(column).setCellValue(j++);
            row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 1).setCellValue("0650");
            row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 2).setCellValue("");
            row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 3).setCellValue("");
            row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 4).setCellValue("");
            row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 5).setCellValue("");
            row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 6).setCellValue(sadere.getLong(14));
            row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        }
    }

    public void payOffFacilitiesInterest(ResultSet sadere) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند سود تسهیلات دریافتی در سامانه نامی");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
    }


}

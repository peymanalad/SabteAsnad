package com.example.excelreader.sabteAsnad.shetab.service;

import com.example.excelreader.sabteAsnad.Helper;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@Service
public class ShetabService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public ShetabService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("شتاب");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;

    }

    public void createShetabExcel() throws SQLException, IOException {

        String beyneBanki = "select amount from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and debit_party = 'NOORIRTHXXX' " +
                "and trn like '%2234'";

        /*String shetabBedehkar = "select * from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and debit_party = 'NOORIRTHXXX'";

        String shetabBestankar = "select * from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and credit_party = 'NOORIRTHXXX'";*/

        PreparedStatement beyneBankiStatement = Helper.getConnection().prepareStatement(beyneBanki);
        /*PreparedStatement bedehkarStatement = Helper.getConnection().prepareStatement(shetabBedehkar);
        PreparedStatement bestankarStatement = Helper.getConnection().prepareStatement(shetabBestankar);*/
        ResultSet beyneBankiResultSet = beyneBankiStatement.executeQuery();
        beyneBankiResultSet.next();
        Long beyneBankiAmount = beyneBankiResultSet.getLong("amount");


        beyneBanki(beyneBankiAmount);
        karmozd();
        moghayerat();

        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Excel.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void beyneBanki(Long value) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند بین بانکی شتاب");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        Row row = sheet.createRow(this.rowCount++);
        row.createCell(column).setCellValue(j++);
        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 1).setCellValue("8888");
        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 2).setCellValue("02309001");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("حساب بین بانکها/از طریق مرکز .ش");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue("ثبت سند بین بانکی شتاب مورخ ");
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue(String.valueOf(value));
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 6).setCellValue("");
        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j++);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("8888");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("12100202");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("هـ/متمرکز سیستم صادره");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("ثبت سند بین بانکی شتاب مورخ ");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue("");
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 6).setCellValue(String.valueOf(value));
        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row3 = sheet.createRow(this.rowCount++);
        row3.createCell(column).setCellValue(j++);
        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 1).setCellValue("0650");
        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 2).setCellValue("121000201");
        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 3).setCellValue("هـ/متمرکز سیستم وارده");
        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 4).setCellValue("ثبت سند بین بانکی شتاب مورخ ");
        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 5).setCellValue(String.valueOf(value));
        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 6).setCellValue("");
        row3.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row4 = sheet.createRow(this.rowCount++);
        row4.createCell(column).setCellValue(j);
        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 1).setCellValue("0650");
        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 2).setCellValue("00800101");
        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 3).setCellValue("حساب جاری نزد بانک 26850/50ش");
        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 4).setCellValue("ثبت سند بین بانکی شتاب مورخ ");
        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 5).setCellValue("");
        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 6).setCellValue(String.valueOf(value));
        row4.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row5 = sheet.createRow(this.rowCount++);
        row5.createCell(column + 4).setCellValue("جمع کل");
        row5.getCell(column + 4).setCellStyle(Helper.createHeaderStyle(headerStyle));
        row5.createCell(column + 5).setCellValue(String.valueOf(Long.parseLong(row.getCell(5).getStringCellValue()) + Long.parseLong(row3.getCell(5).getStringCellValue())));
        row5.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row5.createCell(column + 6).setCellValue(String.valueOf(Long.parseLong(row2.getCell(6).getStringCellValue()) + Long.parseLong(row4.getCell(6).getStringCellValue())));
        row5.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

    public void karmozd() {

    }

    public void moghayerat() {

    }

}
